from locust import HttpUser, task, between, events, TaskSet
import json
import random
import datetime
import random
import logging
from scenarios_script.create_dtos import user_dto, reservation_create_dto, borrowing_create_dto, fake_author_dto, book_create_dto, book_update_dto, fince_create_dto
from ids_storage_model import ids_storage
from taks_sets import FindConcreteTaskSet, DeleteInvalidTaskSet, GetAllTaskSet, BorrowingsTaskSet, ReservationsTaskSet, AuthorsTaskSet, BookTaskSet, FineTaskSet


class FetchExistingData(HttpUser):
    wait_time = between(0.1, 0.5)
    fixed_count = 1  # Only one admin will be spawned
    executed = False

    def on_start(self):
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


    def on_stop(self):
        print(f'Users {ids_storage.user_ids}')
        print(f'Books {ids_storage.book_ids}')
        print(f'Authors {ids_storage.authors_ids}')
        print(f'Borrowings {ids_storage.borrowings_ids}')
        print(f'Reservations {ids_storage.reservations_ids}')
        print(f'Instances {ids_storage.instances_ids}')
        print(f'Fines {ids_storage.fines_ids}')
        print(f'Payments {ids_storage.payments_ids}')
    
    # Task to make this class work
    @task
    def find_user(self):
        if not self.executed:
            self.client.get(f'/users/{random.choice(ids_storage.instances_ids)}')


class LibraryUser(HttpUser):
    wait_time = between(1, 2)
    fixed_count = 2
    tasks = [FindConcreteTaskSet,
             GetAllTaskSet, 
             BorrowingsTaskSet,
             BorrowingsTaskSet,
             ReservationsTaskSet,
             ReservationsTaskSet]
    # tasks = [GetAllTaskSet]

    # Every new testing user creates new user in system
    def on_start(self):
        with self.client.post('/users', json=user_dto()) as response:
            ids_storage.user_ids.append(response.json()['id'])




class AdminUser(HttpUser):
    wait_time = between(1, 4)
    fixed_count = 2
    tasks = [DeleteInvalidTaskSet,
             BookTaskSet, 
             BookTaskSet,
             AuthorsTaskSet,
             AuthorsTaskSet,  
             FineTaskSet,
             FineTaskSet]





