# Locust scenarios
### Requirements:
 - Python >= 3.7
 - pip3

### Setup
```sh
pip install -r requirements.txt
```
Then based on project readme run podman-compose.
```sh
podman-compose build
podman-compose up [-d]
```
### Run
in root directory ```pa165-library-management-system``` 
```sh
locust -f scenarios_script/library_management_locust.py --autostart -u 3
```
You can see meanwhile results on ```http://localhost:8089``` where locust provide nice GUI.
