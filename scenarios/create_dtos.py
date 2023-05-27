from faker import Faker
import datetime
import random
fake = Faker()
Faker.seed(0)


def user_dto():
    return {
        "username": fake.simple_profile()['username'],
        "password": fake.password(),
        "email": fake.ascii_email(),
        "firstName": fake.first_name(),
        "lastName": fake.last_name(),
        "country": fake.country_code(),
        "city": fake.city(),
        "street": fake.street_name(),
        "houseNumber": fake.building_number(),
        "zip": fake.postcode()
    }


def fake_author_dto():
    return {
        "name": fake.first_name(),
        "surname": fake.last_name()
    }


def reservation_create_dto(userId, bookId):
    from_date = datetime.datetime.now()
    to_date = from_date + datetime.timedelta(days=random.randint(1, 20))
    return {
        "userId": userId,
        "bookId": bookId,
        "reservedFrom": str(from_date).replace(' ', 'T'),
        "reservedTo": str(to_date).replace(' ', 'T')
    }


def borrowing_create_dto(userId, instanceId):
    from_date = datetime.datetime.now()
    to_date = from_date + datetime.timedelta(days=random.randint(1, 20))
    return {
        "userId": userId,
        "bookInstanceId": instanceId,
        "borrowedFrom": str(from_date).replace(' ', 'T'),
        "borrowedTo": str(to_date).replace(' ', 'T')
    }


def book_create_dto(authors_ids):
    return {
        "title": fake.text(max_nb_chars=20),
        "authorIds": authors_ids
    }


def book_update_dto(bookDtoResponse):
    return {
        "title": bookDtoResponse['title'] + fake.word(),
        "authorIds": [sub['id'] for sub in bookDtoResponse['authors']],
    }


def fine_create_dto(borrowing_id, issuer_id):
    return {
        "amount": random.randint(50, 300),
        "outstandingBorrowingId": borrowing_id,
        "issuerId": issuer_id
    }


def transaction_create_dto():
    return {
        "amount": random.randint(50, 300),
        "callbackUrl": fake.hostname()
    }


def payment_card_dto():
    return {
        "cardNumber": fake.credit_card_number(),
        "expiration": fake.credit_card_expire(),
        "cvv2": fake.credit_card_security_code()
    }
