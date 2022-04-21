package server.observers

import server.abstractions.Res

interface AnyObserver<R : Res> {
    fun update(domain: R)
}
