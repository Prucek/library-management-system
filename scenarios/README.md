# Locust scenarios

## Requirements:

- Python >= 3.7
- pip
- locust
- Faker

## Setup

Install dependencies (optionally inside
a [virtual environment](https://docs.python.org/3/library/venv.html)):

```sh
pip install -r scenarios/requirements.txt
```

Then based on project readme run `podman-compose`:

```sh
podman-compose build
podman-compose up [-d]
```

## Run Scenarios

```sh
locust -f scenarios/library_management_locust.py --autostart -u 3
```

You can see meanwhile results on `http://localhost:8089` where locust provide
nice GUI.
