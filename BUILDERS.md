# Lullaby Builder Methods Reference

A concise reference guide explaining the builder and facade methods available in the Lullaby game engine.

---

## Table of Contents
1. [Lullaby & LullabyGame (Game & Facades)](#1-lullaby--lullabygame-game--facades)
2. [Scene (Story & Content Builder)](#2-scene-story--content-builder)
3. [Choice (Option & Action Builder)](#3-choice-option--action-builder)
4. [Field (Text Input & Override Builder)](#4-field-text-input--override-builder)
5. [ItemClass & Stat (Entity Builders)](#5-itemclass--stat-entity-builders)
6. [Time & TimeConstructor (Time Builder)](#6-time--timeconstructor-time-builder)

---

## 1. Lullaby & LullabyGame (Game & Facades)

### Static Creation
- `Lullaby.create()`: Instantiates a new `LullabyGame`.
- `Lullaby.create(String name)`: Instantiates, names, registers, and returns a new `LullabyGame`.
- `Lullaby.get(String name)`: Retrieves a registered game by its name.
- `Lullaby.getDefault()`: Returns the default game instance or creates one if none exists.

### Game Configuration Builders
- `name(String name)` / `setName(String name)`: Sets the title / name of the game.
- `saveFileVersion(String version)` / `setSaveFileVersion(String version)`: Sets the save file version.
- `author(String author)` / `setAuthor(String author)`: Sets the game's author.
- `startingScene(String sceneId)` / `setStartingSceneName(String sceneId)`: Sets the initial scene when starting the game.
- `icon(String iconPath)` / `setIconPath(String iconPath)`: Sets the window / application icon path.
- `displayTime(boolean display)` / `setShouldDisplayTime(boolean display)`: Toggles time display in the sidebar.
- `windowSize(int width, int height)` / `setWindowSize(int width, int height)`: Sets the game window dimensions.
- `windowSizeX(int width)` / `windowSizeY(int height)`: Sets individual window width or height.
- `useScenePrefixByDefault(String prefix)`: Configures a default scene ID prefix.
- `excludeSceneFromRecords(String sceneId)`: Excludes a scene from history / record tracking. (NON FUNCTIONAL)

### Scene Management Facades
- `scene(String id)`: Retrieves or creates a `Scene` with the given ID.
- `scene(Scene scene)` / `addScene(Scene scene)`: Adds an existing `Scene` instance to the game.
- `getScene(String id)`: Fetches a scene by ID.
- `hasScene(String id)`: Checks if a scene exists.
- `goToScene(String sceneId)`: Navigates immediately to the specified scene.

### Game State & Variable Facades
- `setPlayerName(String name)` / `getPlayerName()`: Sets and gets the player's name.
- `setVar(String key, Object value)` / `getVar(String key)`: Sets and gets general variables.
- `getVar(String key, Class<T> type)` / `getVar(String key, T defaultValue)`: Type-safe variable retrieval with optional default value.
- `isVarEqual(String key, Object value)`: Checks whether a variable matches an expected value.
- `hasVar(String key)` / `removeVar(String key)`: Checks for or deletes a variable.
- `setFlag(String key, boolean value)` / `getFlag(String key)` / `toggleFlag(String key)`: Boolean flag helpers.
- `setCounter(String key, int value)` / `getCounter(String key)`: Integer counter helpers.
- `incrementCounter(String key)` / `incrementCounter(String key, int amount)`: Increments a counter by 1 or a given amount.
- `decrementCounter(String key)` / `decrementCounter(String key, int amount)`: Decrements a counter by 1 or a given amount.
- `setString(String key, String value)` / `getString(String key)`: String variable helpers.

### Stats Facades
- `addStat(Stat stat)` / `addStat(String id, String displayName, int initialValue)`: Registers a new player stat.
- `setStat(String id, int value)` / `getStat(String id)` / `getStatValue(String id)`: Sets and retrieves stat values.
- `modifyStat(String id, int delta)`: Modifies a stat by adding delta (e.g. `+10` or `-5`).
- `hasStat(String id)`: Checks if a stat exists.

### Inventory & Items Facades
- `registerItem(ItemClass itemClass)` / `registerItem(String id, String name, String desc)`: Registers an item definition.
- `giveItem(String classPointer, int quantity)` / `giveItem(Item item)`: Adds items to the player's inventory.
- `removeItem(String classPointer, int quantity)` / `removeItem(Item item)`: Removes items from inventory.
- `hasItem(String classPointer)` / `hasItem(String classPointer, int quantity)`: Checks item ownership.
- `getItemQuantity(String classPointer)`: Returns current item quantity.

### Time Facades
- `setTime(Time time)` / `setTime(long seconds)`: Sets the current in-game time.
- `advanceTime(long seconds)` / `addTime(long seconds)`: Advances in-game time by the given seconds.
- `getTime()` / `getFormattedTime()`: Retrieves the `Time` object or formatted string (`Day X, H:M`).

---

## 2. Scene (Story & Content Builder)

### Creation & Metadata
- `Scene.make(String id)` / `Scene.create(String id)` / `Scene.of(String id)`: Factory methods to instantiate a scene.
- `title(String title)` / `title(Function<GameState, String>)`: Sets the scene's header title (static or dynamic).
- `titleIf(Predicate<GameState>, ...)`: Sets title conditionally.
- `isEnding(boolean isEnding)` / `ending()`: Marks the scene as a story ending.
- `onEnter(Consumer<GameState>)`: Registers an action executed when entering the scene.
- `excludeFromRecords(boolean)`: Toggles recording this scene visit in player history.

### Text Blocks
- `text(String text)` / `text(Function<GameState, String>)`: Adds a text block (static or dynamically generated from `GameState`).
- `textIf(Predicate<GameState>, ...)`: Displays text if custom condition is met.
- `textIfFlag(String flag, ...)` / `textIfFlagFalse(String flag, ...)`: Displays text based on a boolean flag.
- `textIfVarEquals(String var, Object val, ...)` / `textIfVarNotEqual(...)`: Displays text based on variable equality.
- `textIfFirst(...)`: Displays text only on the first visit to this scene.
- `textIfIteration(int count, ...)` / `textIfIteration(Predicate<Integer>, ...)`: Displays text on specific visit counts.
- `textIfHasItem(String item, ...)` / `textIfDoesntHaveItem(String item, ...)`: Displays text conditionally on item possession.

### Choices & Fields
- `choice(Choice choice)` / `choice(String label, String targetSceneId)`: Adds a choice button leading to another scene.
- `choice(String label, String targetSceneId, Consumer<GameState> action)`: Adds a choice with an execution action.
- `choice(Function<GameState, String> dynamicLabel, String targetSceneId)`: Adds a choice with dynamically rendered text.
- `choiceIf(Predicate<GameState>, ...)`: Adds a choice with display conditions.
- `field(Field field)` / `field(String placeholder, String targetSceneId)`: Adds an input text field.
- `field(String placeholder, String targetSceneId, String boundVariableKey)`: Adds an input field bound to a variable.
- `fieldIf(Predicate<GameState>, ...)`: Adds an input field conditionally.

### Images
- `image(Object/String/File/Image/ImageIcon/URL/Function)`: Adds an image element to the scene.
- `imageIf(Predicate<GameState>, ...)`: Adds an image conditionally.
- `imageIfFlag(...)` / `imageIfFlagFalse(...)`: Shows image based on flag.
- `imageIfVarEquals(...)` / `imageIfVarNotEqual(...)`: Shows image based on variable comparison.
- `imageIfFirst(...)` / `imageIfIteration(...)`: Shows image based on scene visit count.
- `imageIfHasItem(...)` / `imageIfDoesntHaveItem(...)`: Shows image based on inventory possession.

### Scoped Conditional Content (`when`)
- `when(Predicate<GameState>, Consumer<SceneConditionScope>)`: Scopes multiple scene elements (text, images, choices, fields) to be conditionally displayed under a single shared condition predicate.
- `whenFlag(String, Consumer<SceneConditionScope>)` / `whenFlagFalse(...)`: Scoped elements displayed based on boolean flag.
- `whenVarEquals(String, Object, Consumer<SceneConditionScope>)` / `whenVarNotEqual(...)`: Scoped elements based on variable equality.
- `whenHasItem(String, ...)` / `whenDoesntHaveItem(...)`: Scoped elements based on item possession.
- `whenStatGreaterThan(String, int, ...)` / `whenStatLessThan(...)` / `whenStatEquals(...)`: Scoped elements based on player stats.
- Inside the `SceneConditionScope`, call `.text(...)`, `.image(...)`, `.choice(...)`, `.field(...)`, or nested `.when(...)` without repeating conditions.

---

## 3. Choice (Option & Action Builder)

### Creation & Labels
- `Choice.make(String label, String target)` / `Choice.of(...)` / `Choice.create(...)`: Creates a choice.
- `label(String label)` / `setLabel(String label)`: Sets button label text.
- `dynamicLabel(Function<GameState, String>)` / `setDynamicText(...)`: Sets dynamic label generator.
- `target(String targetSceneId)` / `targetSceneId(String targetSceneId)`: Sets target destination scene.

### Execution Actions
- `onSelect(Consumer<GameState>)`: Attaches an action to run when the choice is clicked.
- `setVar(String key, Object val)`: Sets a variable on click.
- `setFlag(String key, boolean val)`: Sets a flag on click.
- `setCounter(String key, int val)` / `incrementCounter(String key)`: Updates a counter on click.
- `setString(String key, String val)`: Updates a string variable on click.
- `setStat(String statId, int val)` / `modifyStat(String statId, int delta)`: Updates a stat on click.
- `giveItem(String item, int quantity)` / `givesItem(...)`: Gives an item to the player on click.
- `consumesItem(String item, int quantity)`: Requires and removes an item on click.
- `advanceTime(long seconds)`: Advances time on click.

### Visibility & Requirements
- `displayIf(Predicate<GameState>)`: Displays choice only if condition evaluates to true.
- `hiddenIf(Predicate<GameState>)`: Hides choice if condition is true.
- `displayIfFlag(String)` / `hiddenIfFlag(String)`: Visibility based on boolean flag.
- `displayIfVarEquals(String, Object)` / `hiddenIfVarEquals(...)`: Visibility based on variable match.
- `displayIfHasItem(String, int)` / `hiddenIfHasItem(...)`: Visibility based on item ownership.
- `requiresItem(String item, int quantity)`: Restricts choice visibility to when player possesses the item.

---

## 4. Field (Text Input & Override Builder)

### Creation & Configuration
- `Field.make(...)` / `Field.of(...)` / `Field.create(...)`: Factory methods to create input fields.
- `placeholder(String placeholder)`: Sets the prompt text shown inside the text field.
- `target(String targetSceneId)` / `targetScene(String targetSceneId)`: Sets default destination scene on submission.
- `bindTo(String variableKey)` / `putAsVar(String variableKey)`: Automatically stores submitted text into a variable.
- `putAsString(String key)` / `putAsInt(String key)` / `putAsBoolean(String key)` / `putAsStat(String key)`: Parses and saves input as specified data type.
- `onEntered(Consumer<GameState>)` / `onEntered(BiConsumer<GameState, String>)`: Custom submission handler.

### Conditional Destination Overrides
- `sceneOverrideIf(Predicate<GameState>, String targetScene)`: Changes target scene based on game state.
- `sceneOverrideIf(BiPredicate<GameState, String>, String targetScene)`: Changes target scene based on state and input.
- `sceneOverrideIfInputEquals(String text, String targetScene)`: Changes target scene if input exactly matches text.
- `sceneOverrideIfInputMatches(Predicate<String>, String targetScene)`: Changes target scene if input satisfies matcher.
- `sceneOverrideIfFlag(...)` / `sceneOverrideIfVarEquals(...)` / `sceneOverrideIfHasItem(...)`: State-based scene overrides.

### Visibility
- `displayIf(Predicate<GameState>)` / `hiddenIf(Predicate<GameState>)`: Configures field visibility.

---

## 5. ItemClass & Stat (Entity Builders)

### ItemClass
- `ItemClass.make(String id, String displayName, String description)` / `ItemClass.create(...)`: Creates an item class.
- `id(String id)`: Sets item identifier.
- `displayName(String displayName)`: Sets user-facing name.
- `description(String description)`: Sets item description.

### Stat
- `Stat.make(String id, String displayName, int value)` / `Stat.create(...)`: Creates a stat.
- `id(String id)` / `identifier(String id)`: Sets stat identifier.
- `displayName(String displayName)`: Sets user-facing display name.
- `value(int value)` / `withValue(int value)`: Sets stat value.

---

## 6. Time & TimeConstructor (Time Builder)

- `Time.construct()` / `Time.builder()`: Returns a `TimeConstructor` builder instance.
- `Time.of(long seconds)`: Creates a `Time` instance directly with seconds.
- `seconds(long seconds)`: Adds seconds.
- `minutes(long minutes)`: Adds minutes (converted to seconds).
- `hours(long hours)`: Adds hours (converted to seconds).
- `days(long days)`: Adds days (converted to seconds).
- `weeks(long weeks)`: Adds weeks (converted to seconds).
- `construct()` / `build()`: Builds and returns the final `Time` object.
