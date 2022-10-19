
# Androlibs App (https://androlibs.com) 🔥❤❤🔥😎
Android App for Androlibs.com

Androlibs is best destination to get the android libraries in the world. Developers can find libraries or can submit on it.

#Tech Stack
# Paging 3


Paging 3 is significantly different from earlier versions of the Paging library. This version provides enhanced functionality and addresses common difficulties with using Paging 2. If your app already uses an earlier version of the Paging library, read this page to learn more about migrating to Paging 3.

If Paging 3 is the first version of the Paging library that you are using in your app, see Load and display paged data for basic usage information.

--> # Benefits of migrating to Paging 3
Paging 3 includes the following features that were not present in earlier versions of the library:

First-class support for Kotlin coroutines and Flow.
Support for async loading using RxJava Single or Guava ListenableFuture primitives.
Built-in load state and error signals for responsive UI design, including retry and refresh functionality.
Improvements to the repository layer, including cancellation support and a simplified data source interface.
Improvements to the presentation layer, list separators, custom page transforms, and loading state headers and footers.
/------------------------------------------------------------------------------------------------------------------------/

# Flow

In coroutines, a flow is a type that can emit multiple values sequentially, as opposed to suspend functions that return only a single value. For example, you can use a flow to receive live updates from a database.

Flows are built on top of coroutines and can provide multiple values. A flow is conceptually a stream of data that can be computed asynchronously. The emitted values must be of the same type. For example, a Flow<Int> is a flow that emits integer values.

A flow is very similar to an Iterator that produces a sequence of values, but it uses suspend functions to produce and consume values asynchronously. This means, for example, that the flow can safely make a network request to produce the next value without blocking the main thread.

There are three entities involved in streams of data:

A producer produces data that is added to the stream. Thanks to coroutines, flows can also produce data asynchronously.
(Optional) Intermediaries can modify each value emitted into the stream or the stream itself.
A consumer consumes the values from the stream.

/------------------------------------------------------------------------------------------------------------------------/
  
 # Nagivation Components

Navigation graph: An XML resource that contains all navigation-related information in one centralized location. This includes all of the individual content areas within your app, called destinations, as well as the possible paths that a user can take through your app.
NavHost: An empty container that displays destinations from your navigation graph. The Navigation component contains a default NavHost implementation, NavHostFragment, that displays fragment destinations.
NavController: An object that manages app navigation within a NavHost. The NavController orchestrates the swapping of destination content in the NavHost as users move throughout your app.

/------------------------------------------------------------------------------------------------------------------------/

 # Livedata
  LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.
  
    LiveData considers an observer, which is represented by the Observer class, to be in an active state if its lifecycle is in the STARTED or RESUMED state. LiveData only notifies active observers about updates. Inactive observers registered to watch LiveData objects aren't notified about changes.

You can register an observer paired with an object that implements the LifecycleOwner interface. This relationship allows the observer to be removed when the state of the corresponding Lifecycle object changes to DESTROYED. This is especially useful for activities and fragments because they can safely observe LiveData objects and not worry about leaks—activities and fragments are instantly unsubscribed when their lifecycles are destroyed.

For more information about how to use LiveData, see Work with LiveData objects.

The advantages of using LiveData
Using LiveData provides the following advantages:

Ensures your UI matches your data state
LiveData follows the observer pattern. LiveData notifies Observer objects when underlying data changes. You can consolidate your code to update the UI in these Observer objects. That way, you don't need to update the UI every time the app data changes because the observer does it for you.
No memory leaks
Observers are bound to Lifecycle objects and clean up after themselves when their associated lifecycle is destroyed.
No crashes due to stopped activities
If the observer's lifecycle is inactive, such as in the case of an activity in the back stack, then it doesn’t receive any LiveData events.
No more manual lifecycle handling
UI components just observe relevant data and don’t stop or resume observation. LiveData automatically manages all of this since it’s aware of the relevant lifecycle status changes while observing.
Always up to date data
If a lifecycle becomes inactive, it receives the latest data upon becoming active again. For example, an activity that was in the background receives the latest data right after it returns to the foreground.
Proper configuration changes
If an activity or fragment is recreated due to a configuration change, like device rotation, it immediately receives the latest available data.
Sharing resources
You can extend a LiveData object using the singleton pattern to wrap system services so that they can be shared in your app. The LiveData object connects to the system service once, and then any observer that needs the resource can just watch the LiveData object. For more information, see Extend LiveData.

 /------------------------------------------------------------------------------------------------------------------------/

  
  # Contents
This Application is developed Kotlin. It use modern day architecture pattern MVVM.
It also uses Coroutines, Paging, Navigation components ect.

You can use this in your projects but currently API is not given as I am working on it.


##
<p float="left">
  <img src="https://raw.githubusercontent.com/vedraj360/Androlibs.com-App/master/screenshots/s1.png" width="200"   hspace="50"/>
    <img src="https://raw.githubusercontent.com/vedraj360/Androlibs.com-App/master/screenshots/s2.png" width="200"   hspace="50"/>
</p>

##

<p>
      <img src="https://raw.githubusercontent.com/vedraj360/Androlibs.com-App/master/screenshots/s3.png" width="200"   hspace="50"/>
        <img src="https://raw.githubusercontent.com/vedraj360/Androlibs.com-App/master/screenshots/s4.png" width="200"   hspace="50"/>
</p>
