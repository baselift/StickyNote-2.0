# StickyNote-2.0
## Description
This was a personal project in Java that acts like a Sticky Note, so you can jot down daily plans, ideas etc... If anyone were to come across this and consider using it, note this was mostly made for me to use, so I am sorry if my app did not accommodate your needs.
However, you could implement any missing features for yourself.

## What was version 1.0?
I actually thought of this idea in 2022, I initially planned to make some sticky note app somehow, and have it appear on my desktop on startup, so I could remind myself of stuff I needed to do. However, the first version was based on the user typing all their text at once (it could be edited later), then my program would draw the text on the screen. But the problem was that actions like copy, paste and newline were signficantly harder to implement relative to just using a JTextArea. So I decided to switch over to using JTextArea in the 2.0 version.

## Next Plans
High Priority:
- Code a loader (maybe in a different repo) that will load all sticky notes from a folder at startup (now we can have a weekly planner!)
- If implementing the feature above, then will need to add Sticky Note save support
- Any bugs I find (there is bound to be some)

Medium Priority:
- Add resizing support

Lower Priority:
- Add undo/redo support in text box
- Add scroll bar support
- Add font change support
- Potentially other features

## Reflection
There were various choices that were made, and the final result was the probably the simplest version. Features like scroll bar is not high priority because for me I do not need the scroll bar, although if it was made for a different audience, it could be more important.
I would be open to improvements and suggestions regarding optimizations, bugs and feature requests (although it is not guaranteed that I will actually act on them, I highly recommended to fork this repo if you want to add new features or make major changes).
