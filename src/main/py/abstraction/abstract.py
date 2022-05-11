from pydantic import BaseModel
from typing import List


class UserInfoQuery(BaseModel):
    id: int


class UserInfoAddCommand(BaseModel):
    id: int
    data: List[str]


class UserInfoDeleteCommand(BaseModel):
    id: int
