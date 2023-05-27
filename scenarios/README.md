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

Before running, you need an authorization token which you can get from the
OAuth2 client running on the port 8080 by default. Make sure to get both test
scopes.

Linux:
```sh
LOCUST_TOKEN=<INSERT_AUTH_TOKEN_HERE> locust -f scenarios/library_management_locust.py --autostart -u 6
```

Windows with Powershell:
```ps
$env:LOCUST_TOKEN = <INSERT_AUTH_TOKEN_HERE>
locust -f scenarios/library_management_locust.py --autostart -u 6
```


You can see meanwhile results on `http://localhost:8089` where locust provide
nice GUI.
