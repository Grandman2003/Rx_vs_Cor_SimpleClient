package com.example.retrsample2.views

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface ShowStudentsView: MvpView {
    fun showStudents(string: String)
    fun studentAdded()
    fun showError(message: String)
}