# zinc-issue-810

Reproducer for an issue with zinc incremental compilation https://github.com/sbt/zinc/issues/810

## Reproduce

* launch sbt
* do a clean compile `;clean;compile`
* all subsequent compiles should detect no changes and compile nothing

```
sbt:scala-seed-zinc-problem> compile
[debug] Other repositories:
[debug] Default repositories:
[debug] Using inline dependencies specified in Scala.
[debug] No changes
[debug] Copy resource mappings: 
[debug] 
[debug] No changes
[success] Total time: 0 s, completed Aug 12, 2020, 4:08:47 PM
```
* modify `core/src/main/scala/A.scala` renaming the field `a` to `b`
* `compile`
```
sbt:scala-seed-zinc-problem> compile
[debug] Other repositories:
[debug] Default repositories:
[debug] Using inline dependencies specified in Scala.
[debug] 
[debug] Initial source changes: 
[debug]         removed:Set()
[debug]         added: Set()
[debug]         modified: Set(/home/joao/git/scala-seed-zinc-problem/core/src/main/scala/A.scala)
[debug] Invalidated products: Set()
[debug] External API changes: API Changes: Set()
[debug] Modified binary dependencies: Set()
[debug] Initial directly invalidated classes: Set(core.A)
[debug] 
[debug] Sources indirectly invalidated by:
[debug]         product: Set()
[debug]         binary dep: Set()
[debug]         external source: Set()
[debug] All sources are invalidated.
[debug] Initial set of included nodes: core.A
[debug] Recompiling all sources: number of invalidated sources > 50.0% of all sources
[info] Compiling 1 Scala source to /home/joao/git/scala-seed-zinc-problem/core/target/scala-2.13/classes ...
[debug] Getting org.scala-sbt:compiler-bridge_2.13:1.3.5:compile for Scala 2.13.2
[debug] Copy resource mappings: 
[debug] 
[debug] Getting org.scala-sbt:compiler-bridge_2.13:1.3.5:compile for Scala 2.13.2
[debug] [zinc] Running cached compiler 19694abc for Scala compiler version 2.13.2
[debug] [zinc] The Scala compiler is invoked with:
[debug]         -bootclasspath
[debug]         /home/joao/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.2/scala-library-2.13.2.jar
[debug]         -classpath
[debug]         /home/joao/git/scala-seed-zinc-problem/core/target/scala-2.13/classes
[debug] Scala compilation took 0.296493819 s
[debug] Done compiling.
[debug] The core.A has the following regular definitions changed:
[debug]         UsedName(a,[Default]), UsedName(b,[Default]).
[debug] All member reference dependencies will be considered within this context.
[debug] Files invalidated by inheriting from (external) core.A: Set(); now invalidating by inheritance (internally).
[debug] Getting direct dependencies of all classes transitively invalidated by inheritance.
[debug] Getting classes that directly depend on (external) core.A.
[debug] None of the modified names appears in source file of example.Hello. This dependency is not being considered for invalidation.
[debug] No changes
[success] Total time: 0 s, completed Aug 12, 2020, 4:09:07 PM
```
* code was incrementally compiled correctly
* compile again
```
sbt:scala-seed-zinc-problem> compile
[debug] Other repositories:
[debug] Default repositories:
[debug] Using inline dependencies specified in Scala.
[debug] No changes
[debug] Copy resource mappings: 
[debug] 
[debug] The core.A has the following regular definitions changed:
[debug]         UsedName(a,[Default]), UsedName(b,[Default]).
[debug] All member reference dependencies will be considered within this context.
[debug] Files invalidated by inheriting from (external) core.A: Set(); now invalidating by inheritance (internally).
[debug] Getting direct dependencies of all classes transitively invalidated by inheritance.
[debug] Getting classes that directly depend on (external) core.A.
[debug] None of the modified names appears in source file of example.Hello. This dependency is not being considered for invalidation.
[debug] No changes
[success] Total time: 0 s, completed Aug 12, 2020, 4:09:11 PM
```
* Incorrectly, zinc says that `core.A` changed, and calculates the invalidated files again. However nothing changed since the last compile
* Expected that zinc would state that nothing changed, like the 2nd compile after the clean...



