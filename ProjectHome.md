## What is Concordion? ##

Concordion is a Java framework that lets you write requirements in normal language and turn them into automated tests.

## How does it work? ##

Specifications are written in HTML. This allows specifications to be linked together to form navigable documentation. Each specification has a corresponding Java "fixture" class that is run in the same way as a JUnit test. Methods on the fixture are called by hidden instrumentation in the specification. These methods call the system under test and then flag up successes and failures.

![http://www.concordion.org/image/concept/HowItWorks.png](http://www.concordion.org/image/concept/HowItWorks.png)

## It sounds complicated? ##

The way we've explained it may sound complicated, but in reality it's straight-forward. For a Java developer who is used to writing JUnit tests, it's almost the same except there's an HTML file associated with the test.

Most people are up and running quickly and find it very intuitive. Concordion has been kept purposely small to make it easy to learn. The best place to start is the [tutorial](http://www.concordion.org/Tutorial.html).

## Documentation ##

Full documentation is available on the [Concordion website](http://www.concordion.org).

## Source Code ##

The source code is available in [GitHub](https://github.com/concordion) under the following projects:

  * [concordion](https://github.com/concordion/concordion) - the full Concordion source (including its tests)
  * [concordion-kickstart](https://github.com/concordion/concordion-kickstart) - the source for the tutorial's demo application
  * [concordion-website](https://github.com/concordion/concordion-website) - the source HTML for the Concordion website
  * [concordion-extensions](https://github.com/concordion/concordion-website) - the source for the Concordion Extensions Library
  * [concordion-extensions-demo](https://github.com/concordion/concordion-website) - demonstration of the Concordion Extensions Library