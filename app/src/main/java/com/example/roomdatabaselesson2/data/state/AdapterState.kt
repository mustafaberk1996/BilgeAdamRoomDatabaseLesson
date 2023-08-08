package com.example.roomdatabaselesson2.data.state

sealed class AdapterState{
    object Idle:AdapterState()
    class Removed(val index:Int):AdapterState()
    class Changed(val index:Int):AdapterState()
    class Added(val index:Int):AdapterState()
}
