# ToDoWithListViewAnimations
This project demonstrates how to use ListViewAnimations with CursorAdapter. 

If you enable drag and drop feature with CursorAdapter, it requires some changes to the adatper. Basically, the drag and drop 
involves intermediate modifications to the list and it is not prudent to save those changes to your data source until the user 
completes the drop operation.

The idea here is to remember the changed positions when user drags a row. ListViewAnimations simply moves other rows up or down
when you go on dragging your item above or below in the list. So, the positions of those swapped items change. The extended 
CursorAdapter keeps track of changed positions and returns the right item when list asks for them.
