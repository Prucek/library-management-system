from locust import HttpUser, task, between, events, TaskSet
import json
import random
import datetime
import random
import logging
import os
from scenarios.create_dtos import user_dto, reservation_create_dto, borrowing_create_dto, fake_author_dto, book_create_dto, book_update_dto, fine_create_dto, transaction_create_dto, payment_card_dto
from ids_storage_model import ids_storage
from taks_sets import FindConcreteTaskSet, DeleteInvalidTaskSet, GetAllTaskSet, BorrowingsTaskSet, ReservationsTaskSet, AuthorsTaskSet, BookTaskSet, FineTaskSet


authorization_token = os.environ.get("LOCUST_TOKEN")
if not authorization_token:
    raise EnvironmentError(
        "Authorization token is not passed as an environment variable")


class FetchExistingDataAndAuthorization(HttpUser):
    host = 'http://127.0.0.1:8090'
    wait_time = between(0.1, 0.5)
    fixed_count = 1  # Only one admin will be spawned
    executed = False

    def on_start(self):
        self.client.headers = {'content-type': 'application/json',
                               'Authorization': f'Bearer {authorization_token}'}
        with self.client.get('/users') as response:
            for usr in response.json()['items']:
                ids_storage.user_ids.append(usr['id'])
        # Load existing books
        with self.client.get('/books') as response:
            for book in response.json()['items']:
                ids_storage.book_ids.append(book['id'])
                for book_instances in book['instances']:
                    ids_storage.instances_ids.append(book_instances['id'])
        # Load existing authors
        with self.client.get('/authors') as response:
            for author in response.json()['items']:
                ids_storage.authors_ids.append(author['id'])
        # Load existing reservations
        with self.client.get('/reservations') as response:
            for reserv in response.json()['items']:
                ids_storage.reservations_ids.append(reserv['id'])
        # Load existing borrowings
        with self.client.get('/borrowings') as response:
            for borrowing in response.json()['items']:
                ids_storage.borrowings_ids.append(borrowing['id'])
        # Load existing fines
        with self.client.get('/fines') as response:
            for fine in response.json()['items']:
                ids_storage.fines_ids.append(fine['id'])
        # Load existing payments
        with self.client.get('/payments') as response:
            for payment in response.json()['items']:
                ids_storage.payments_ids.append(payment['id'])
        self.executed = True

    # Task to make this class work
    @task
    def find_user(self):
        if not self.executed:
            self.client.get(
                f'/users/{random.choice(ids_storage.instances_ids)}')


class CommonUser(HttpUser):
    host = 'http://127.0.0.1:8090'
    wait_time = between(1, 2)
    fixed_count = 2

    def on_start(self):
        self.client.headers = {'content-type': 'application/json',
                               'Authorization': f'Bearer {authorization_token}'}

    tasks = [FindConcreteTaskSet,
             GetAllTaskSet,
             BorrowingsTaskSet,
             BorrowingsTaskSet,
             ReservationsTaskSet,
             ReservationsTaskSet]


class Librarian(HttpUser):
    host = 'http://127.0.0.1:8090'

    def on_start(self):
        self.client.headers = {'content-type': 'application/json',
                               'Authorization': f'Bearer {authorization_token}'}

    wait_time = between(1, 4)
    fixed_count = 2
    tasks = [DeleteInvalidTaskSet,
             BookTaskSet,
             BookTaskSet,
             AuthorsTaskSet,
             AuthorsTaskSet,
             FineTaskSet,
             FineTaskSet]


class PaymentGateUser(HttpUser):
    host = 'http://127.0.0.1:8081'
    wait_time = between(1, 4)
    fixed_count = 1

    def on_start(self):
        self.client.headers = {'content-type': 'application/json',
                               'Authorization': f'Bearer {authorization_token}'}

    @task(1)
    def list_payments(self):
        self.client.get(f'/transaction')
        logging.info('Listing all payments')

    @task(2)
    def create_payment(self):
        new_transaction_dto = transaction_create_dto()
        with self.client.post('/transaction', json=new_transaction_dto) as response:
            ids_storage.transactions_unpaid_ids.append(response.json()['id'])
            logging.info(f'Creating transaction {response.json()["id"]}')

    @task(2)
    def pay_transaction(self):
        if ids_storage.transactions_unpaid_ids:
            task_selected_transaction_id = random.choice(
                ids_storage.transactions_unpaid_ids)
            new_card_dto = payment_card_dto()
            with self.client.post(f'/transaction/{task_selected_transaction_id}', json=new_card_dto) as response:
                if response.json()['status'] == 'APPROVED':
                    ids_storage.transactions_unpaid_ids.remove(
                        task_selected_transaction_id)
                    logging.info(
                        f'Transaction {task_selected_transaction_id} was succeesfully paid')
                else:
                    logging.info(
                        f'Transaction {task_selected_transaction_id} was declined, try pay later.')
