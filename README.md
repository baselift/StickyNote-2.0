# StickyNote-2.0
## Description
This was a personal project in Java that acts like a Sticky Note, so you can jot down daily plans, ideas etc... If anyone were to come across this and consider using it, note this was mostly made for me to use, so I am sorry if my app did not accommodate your needs.
However, you could implement it for yourself.

## What was version 1.0?
I actually thought of this idea in 2022, and I made a similar version to this one. However, it was based on the user typing all their text at once (it could be edited later), then my program would draw the text on the screen. But the problem was that
actions like copy, paste and newline were signficantly harder to implement relative to just using a JTextArea. So I decided to switch over to using JTextArea in the 2.0 version.

## Next Plans
High Priority:
- Code a loader (maybe in a different repo) that will load all sticky notes from a folder at startup (now we can have a weekly planner!)
- If implementing the feature above, then will need to add Sticky Note save support

Medium Priority:
- Add resizing support

Lower Priority:
- Add undo/redo support in text box
- Add scroll bar support
- Add font change support

## Reflection
There were various choices that were made, and the final result was the probably the simplest version. Features like scroll bar is not high priority because for me I do not need the scroll bar.
It is entirely possible that some conventions were not followed, especically Swing conventions since this is my first project working with Java Swing (I would be open to improvements and suggestions).
