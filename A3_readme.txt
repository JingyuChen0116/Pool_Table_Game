# Pool Game Builder

=== Overview ===

A single-player pool table game supporting choices for difficulty, UNDO and cheating functionalities.


=== Launching the program ===

To run the application, please use:

gradle run

To generate a javadoc, please use:

gradle javadoc


=== Game Features ===

== Configuration ==

When entering configuration details, please note the following restrictions:
- Friction must be value between 0 - 1 (not inclusive). [Would recommend switching between 0.95, 0.9, 0.85 to see changes].
- Ball X and Y positions must be within the size of the table width and length, including the ball radius (10).
- Ball colours must be Paint string values as expected.
- Pocket X and Y positions must be within the size of the table width and length.

== Difficulty level ==

- The default difficulty level is easy. 
- You can select your difficulty level by clicking the buttons on the top right of the game window at any time.

== Coloured balls ==

The game considers maximum 9 colours for the balls and cheats the white ball as the cue ball.

== Time and score ==

Duration and the current scores are displayed on the right side of the game window.

== Hit the cue ball ==

- The cue stick appears when all the active balls stops moving and rotates as the cursor moves. 
- To hit a ball, move your mouse to the desired direction, drag away and release the cursor to hit the cue ball with the corresponding velocity.

== UNDO ==

The player can undo a change by clicking the 'UNDO' button. The changes which can be revoked are:
- A shot
- A change of difficulty level before starting hitting the cue ball
- A cheat operation

== Cheat ==

The player can remove all the same coloured balls on the table by clicking any button under 'Cheat' label.


=== About Design Patterns ===

== Factory Method ==

- Participant of Creator: ReaderFactory
- Participants of ConcreteCreator: BallReaderFactory, TableReaderFactory, PocketReaderFactory
- Participant of Product: Reader
- Participants of ConcreteProduct: BallReader, TableReader, PocketReader

== Builder Pattern ==

- Participant of Director: BallReader
- Participant of Builder: BallBuilder
- Participant of ConcreteBuilder: PoolBallBuilder
- Participant of Product: Ball

- Participant of Director: PocketReader
- Participant of Builder: PocketBuilder
- Participant of ConcreteBuilder: PoolPocketBuilder
- Participant of Product: Pocket

== Strategy Pattern ==

- Participant of Context: Ball
- Participant of Strategy: PocketStrategy
- Participants of ConcreteStrategy: SingleLifeStrategy, DoubleLifeStrategy, TripleLifeStrategy

== Prototype Pattern ==

- Participant of Prototype: PocketStrategy
- Participants of ConcretePrototype: SingleLifeStrategy, DoubleLifeStrategy, TripleLifeStrategy
- Participant of Client: Ball

== Memento Pattern ==

- Participant of Originator: GameManager
- Participant of CareTake: CareTaker
- Participant of Memento: Memento

== Facade Pattern ==

- Participant of Facade: ConfigParser
- Participants of Subsystem classes: Reader, ReaderFactory
