from locust import task, TaskSet
import random
import logging
import datetime
import random
from ids_storage_model import ids_storage
from scenarios.create_dtos import reservation_create_dto, borrowing_create_dto, fake_author_dto, book_create_dto, book_update_dto, fine_create_dto


class FindConcreteTaskSet(TaskSet):
    @task
    def find_user(self):
        if ids_storage.user_ids:
            self.client.get(f'/users/{random.choice(ids_storage.user_ids)}')
            logging.info('Finding user')

    @task
    def find_book(self):
        if ids_storage.book_ids:
            self.client.get(f'/books/{random.choice(ids_storage.book_ids)}')
            logging.info('Finding book')

    @task
    def find_author(self):
        if ids_storage.authors_ids:
            self.client.get(
                f'/authors/{random.choice(ids_storage.authors_ids)}')
            logging.info('Finding author')

    @task
    def find_borrowing(self):
        if ids_storage.borrowings_ids:
            self.client.get(
                f'/borrowings/{random.choice(ids_storage.borrowings_ids)}')
            logging.info('Finding borrowing')

    @task
    def find_reservations(self):
        if ids_storage.reservations_ids:
            self.client.get(
                f'/reservations/{random.choice(ids_storage.reservations_ids)}')
            logging.info('Finding reservation')

    @task
    def find_fine(self):
        if ids_storage.fines_ids:
            self.client.get(f'/fines/{random.choice(ids_storage.fines_ids)}')
            logging.info('Finding fine')

    @task
    def find_payment(self):
        if ids_storage.payments_ids:
            self.client.get(
                f'/payments/{random.choice(ids_storage.payments_ids)}')
            logging.info('Finding payment')


class GetAllTaskSet(TaskSet):
    @task
    def get_users(self):
        self.client.get(f'/users')
        logging.info('Getting user')

    @task
    def get_books(self):
        self.client.get(f'/books')
        logging.info('Getting books')

    @task
    def get_authors(self):
        self.client.get(f'/authors')
        logging.info('Getting authors')

    @task
    def get_borrowings(self):
        self.client.get(f'/borrowings')
        logging.info('Getting borrowings')

    @task
    def get_reservations(self):
        self.client.get(f'/reservations')
        logging.info('Getting reservations')

    @task
    def get_fines(self):
        self.client.get(f'/fines')
        logging.info('Getting fines')

    @task
    def get_payments(self):
        self.client.get(f'/payments')
        logging.info('Getting payments')


class DeleteInvalidTaskSet(TaskSet):
    @task
    def delete_invalid_user(self):
        with self.client.delete("/users/{}".format('1'), catch_response=True) as response:
            if response.status_code == 404:
                response.success()
            logging.info('Deleting invalid user')

    @task
    def delete_invalid_book(self):
        with self.client.delete("/books/{}".format('1'), catch_response=True) as response:
            if response.status_code == 404:
                response.success()
            logging.info('Deleting invalid book')

    @task
    def delete_invalid_author(self):
        with self.client.delete("/authors/{}".format('1'), catch_response=True) as response:
            if response.status_code == 404:
                response.success()
            logging.info('Deleting invalid author')

    @task
    def delete_invalid_borrowing(self):
        with self.client.delete("/borrowings/{}".format('1'), catch_response=True) as response:
            if response.status_code == 404:
                response.success()
            logging.info('Deleting invalid borrowing')

    @task
    def delete_invalid_reservations(self):
        with self.client.delete("/reservations/{}".format('1'), catch_response=True) as response:
            if response.status_code == 404:
                response.success()
            logging.info('Deleting invalid reservation')

    @task
    def delete_invalid_fines(self):
        with self.client.delete("/fines/{}".format('1'), catch_response=True) as response:
            if response.status_code == 404:
                response.success()
            logging.info('Deleting invalid fine')


class BorrowingsTaskSet(TaskSet):
    @task(1)
    def make_borrowing(self):
        if ids_storage.user_ids and ids_storage.instances_ids:
            task_selected_user_id = random.choice(ids_storage.user_ids)
            task_selected_book_instance_id = random.choice(
                ids_storage.instances_ids)
            with self.client.post(f'/borrowings', json=borrowing_create_dto(task_selected_user_id, task_selected_book_instance_id)) as response:
                ids_storage.borrowings_ids.append(response.json()['id'])
                logging.info(
                    f'Making borrowing for user {task_selected_user_id} on book instance {task_selected_book_instance_id}')

    @task(2)
    def update_borrowing(self):
        if ids_storage.borrowings_ids:
            task_selected_borrowing_id = random.choice(
                ids_storage.borrowings_ids)
            response = self.client.get(
                f'/borrowings/{task_selected_borrowing_id}')
            borrowing_update_dto = {
                "borrowedFrom": response.json()['borrowedFrom'],
                "borrowedTo": str(datetime.datetime.now() + datetime.timedelta(days=random.randint(1, 20))).replace(' ', 'T'),
                "userId": response.json()['user']['id'],
                "bookInstanceId": response.json()['bookInstance']['id'],
                "fine": response.json().get('fine', {}),
                "returned": response.json()['returned']
            }
            self.client.put(
                f'/borrowings/{task_selected_borrowing_id}', json=borrowing_update_dto)
            logging.info(f'Updating borrowing {task_selected_borrowing_id}')


class ReservationsTaskSet(TaskSet):
    @task(2)
    def update_reservation(self):
        if ids_storage.reservations_ids:
            task_selected_reservation_id = random.choice(
                ids_storage.reservations_ids)
            response = self.client.get(
                f'/reservations/{task_selected_reservation_id}')
            reservation_update_dto = {
                "userId": response.json()['user']['id'],
                "bookId": response.json()['book']['id'],
                "reservedFrom": response.json()['reservedFrom'],
                "reservedTo": str(datetime.datetime.now() + datetime.timedelta(days=random.randint(1, 20))).replace(' ', 'T')
            }
            self.client.put(
                f'/reservations/{task_selected_reservation_id}', json=reservation_update_dto)
            logging.info(
                f'Updating reservation for {task_selected_reservation_id}')

    @task(1)
    def make_reservation(self):
        if ids_storage.user_ids and ids_storage.book_ids:
            task_selected_user_id = random.choice(ids_storage.user_ids)
            task_selected_book_id = random.choice(ids_storage.book_ids)
            with self.client.post(f'/reservations', json=reservation_create_dto(task_selected_user_id, task_selected_book_id)) as response:
                ids_storage.reservations_ids.append(response.json()['id'])
                logging.info(
                    f'Making reservation for user {task_selected_user_id} on book {task_selected_book_id}')

    @task(1)
    def delete_reservation(self):
        if ids_storage.reservations_ids:
            task_selected_reservation_id = random.choice(
                ids_storage.reservations_ids)
            self.client.delete(f'/reservations/{task_selected_reservation_id}')
            ids_storage.reservations_ids.remove(task_selected_reservation_id)
            logging.info(
                f'Deleting reservation for {task_selected_reservation_id}')


class AuthorsTaskSet(TaskSet):
    @task(1)
    def make_author(self):
        new_author_dto = fake_author_dto()
        with self.client.post('/authors', json=new_author_dto) as response:
            ids_storage.authors_ids.append(response.json()['id'])
            logging.info(
                f'Creating author {new_author_dto["name"]} {new_author_dto["surname"]}')

    @task(2)
    def update_author(self):
        if ids_storage.authors_ids:
            task_selected_author_id = random.choice(ids_storage.authors_ids)
            response = self.client.get(f'/authors/{task_selected_author_id}')
            new_author_dto = fake_author_dto()
            author_update_dto = {
                "name": response.json()['name'],
                "surname": new_author_dto['surname']
            }
            self.client.put(
                f'/authors/{task_selected_author_id}', json=author_update_dto)
            logging.info(f'Updating author {task_selected_author_id}')


class BookTaskSet(TaskSet):
    @task(2)
    def make_book(self):
        if ids_storage.authors_ids:
            author_ids = []
            # assign 1 - 2 random authors
            for _ in range(random.randint(1, 2)):
                random_author_id = random.choice(ids_storage.authors_ids)
                if random_author_id not in author_ids:
                    author_ids.append(random_author_id)
            # create book
            new_create_dto = book_create_dto(author_ids)
            with self.client.post('/books', json=new_create_dto) as response:
                ids_storage.book_ids.append(response.json()['id'])
                logging.info(f'Creating book {new_create_dto["title"]}')
                # add 1 - 2 instances
                for _ in range(random.randint(1, 2)):
                    with self.client.post(f'/books/{response.json()["id"]}/instances') as responseInstance:
                        ids_storage.instances_ids.append(
                            responseInstance.json()['id'])
                        logging.info(
                            f'Creating instance {responseInstance.json()["id"]} for book {new_create_dto["title"]}')

    @task(2)
    def update_book(self):
        if ids_storage.book_ids:
            task_selected_book_id = random.choice(ids_storage.book_ids)
            with self.client.get(f'/books/{task_selected_book_id}') as response:
                updated_book_dto = book_update_dto(response.json())
                self.client.put(
                    f'/books/{task_selected_book_id}', json=updated_book_dto)
                logging.info(
                    f'Updating book title from {response.json()["title"]} to {updated_book_dto["title"]}')


class FineTaskSet(TaskSet):
    @task(1)
    def make_fine(self):
        if ids_storage.borrowings_ids:
            task_selected_borrowing_id = random.choice(
                ids_storage.borrowings_ids)
            response = self.client.get(
                f'/borrowings/{task_selected_borrowing_id}')
            new_borrowing = fine_create_dto(
                response.json()['id'], response.json()['user']['id'])
            with self.client.post('/fines', json=new_borrowing) as response:
                ids_storage.fines_ids.append(response.json()['id'])
                logging.info(
                    f'Creating fine for borrowing {task_selected_borrowing_id}')

    @task(2)
    def update_fine(self):
        if ids_storage.fines_ids:
            task_selected_fine_id = random.choice(ids_storage.fines_ids)
            response = self.client.get(f'/fines/{task_selected_fine_id}')
            updated_fine_dto = fine_create_dto(
                response.json()['outstandingBorrowing']['id'], response.json()['issuer']['id'])
            self.client.put(
                f'/fines/{task_selected_fine_id}', json=updated_fine_dto)
            logging.info(f'Updating fine {task_selected_fine_id}')
