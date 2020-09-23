## What does this app do

* Gets list of 34 different currencies from server and displays it as list.
* Converts these currencies to Rubles and vice versa.
* Since data on the server updates once a day, app accesses the server **only**
in these three cases: on first launch, when user directly updates it, or during daily syncronization

### What is this app built with

* Kotlin Coroutines for asynchronous operations
* Dagger-android as DI framework
* Retrofit as REST client
* Architecture components (Room, WorkManager, DataBinding, ViewModel, LiveData)
