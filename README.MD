# Android Architecture through a language game
Hey, this is Claudiu 👋 I played with this idea with a few goals in mind
- playing around more with coroutines, making the async functionality easily adaptable and testable
- brainstorming some inter-"process"(or use case) communications and where to hold that logic
    - e.g. even though you have functions `x` and `y`, they might both use `z` for cached data
    - there's multiple paths one can follow
- making testing possible through coroutines

Status:
 DONE: 
    - Architecture + tests (use case, repo, vm) implemented. 
    - ViewModel responsible for game state thoroughly tested
    - Game implemented

#### Description
The task is to write a small language game. The player will see a word in language „one“ on the 
screen. While this word is displayed, a word in language „two“ will fall down on the screen. The 
player will have to choose if the falling word is the correct translation or a wrong translation. 
The player needs to answer, before the word reaches the bottom of the screen. Include a counter 
that gives the player feedback.

- Room for creativity
It is up to the applicant to implement how the player chooses correct or wrong. Also when or if a 
game ends. In what way correct/wrong/no answer is counted can also be freely chosen, but there must 
be some visual feedback.

## Architecture and setup
Still WIP, brainstorming more details

### Needs (beyond hello world):
- caching data from repository
- abstract getting the data so retrofit not needed at definition level

### Architecture:
- Interfaces and abstraction as much as possible to be able to swap implementations in all places 
easily
- Actual architecture (Use cases, Repos and View Models all have base interface definitions that then get implemented):
#### 1. Use cases for actual use case implementation 
    - unaware of Repo using it
    - the cell of the app (sort of)
    - Test Dispatchers injected, along with different individual architecture functionality, based 
    on the Use case details
    - Caching can be implemented as a state within the use case, or the use case can communicate 
    with a `SomeAppCache?` (possibly extending `MyService`) for caching and inter-usecase data, like
    so
    ```
    override suspend fun getPerson(id: String) {
        if(someDataCache?.hasData(id)){
            return someDataCache?.getData(id)
        }
        // at this point cache doesn't return anything for the data
        return service.getData()
    }
    ```
    That caching logic can be further abstracted into some 
    `CacheEnabledService<T : BaseService>(val realService: T)` that would itself get data from the 
    cache before calling `realService` , and would automatically save to cache when 
    inserting to `realService`, propagating all calls to the actual service 
    
#### 2. Repository for orchestrating use cases
    - Aware of use cases, not of VM
    - Some inter-usecase states could also be managed here if needed, in a separate caching Use case
    - Or in properties in the repository
#### 3. ViewModel for UI - Repo communication. UI State is also maintained here without it being aware of the UI
    - Aware of Repo, not of UI
    - Exposes functions it expects the user to make, which work with the existing state and repository(ies)
    - Exposes livedata for observing changes to the state of the VM
#### 4. UI. Aware of ViewModel only
    - Aware of Vm, not of Repo or use case
    - Application creates DI tree
    - Sends actions to VM through calls in the VM (defined in the VM interface)
    - Can get data from VM through 
        - the return value of the defined actions (e.g. `override suspend fun getX(): LiveData<List<x>>`, 
        - livedata properties defined in the VM interface that get updated independently of them 
        being queried, e.g. 
        ```
        /**
        returns some live data containing a value that is changed throughout other VM calls. 
        **/
        override fun getXLiveData() : LiveData<List<X>> = xLiveData
        ```

### Setup
Pull, sync gradle and build

#### Implementation details:
- async functionality
    - business logic: 
        - suspend functions + different dispatchers used throughout the app 
        - check out `DispatcherProviders` class and `TestDispatcherProvider` which runs blocking
    - UI: 
        - callbacks through `LiveData` and `Fragment.viewLifecycleOwner`
        - state of async calls can be sent to the UI by wrapping the `<T>` `LiveData` type in a 
        `AsyncResource<T>` which itself has states for Idle, Loading, Error and Success and 
        properties relevant to those states. We set those states up in the ViewModel
- UI:
    - Single activity
    - Nav controller or manually managing fragments on a back stack
- DI: Koin
- Net: Not needed, if needed: OkHttp, Retrofit, Gson, suspend functions

