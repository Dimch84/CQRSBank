import requests
from typing import List

from abstraction.abstract import UserInfoQuery, UserInfoAddCommand, UserInfoDeleteCommand


headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}


def request_to(addr: str, json: str):
    return requests.post(addr, data=json, headers=headers).text


def get_info(id: int):
    return request_to("http://localhost:8080/userInfoCommands/info", UserInfoQuery(id=id).json())


def add_info(id: int, data: List[str]):
    return request_to("http://localhost:8080/userInfoCommands/add", UserInfoAddCommand(id=id, data=data).json())


def delete_info(id: int):
    return request_to("http://localhost:8080/userInfoCommands/delete", UserInfoDeleteCommand(id=id).json())


if __name__ == '__main__':
    print(get_info(4))
    print(add_info(4, ["msg1", "msg2"]))
    print(add_info(4, ["msg3"]))
    print(get_info(4))
    print(delete_info(4))
    print(get_info(4))
