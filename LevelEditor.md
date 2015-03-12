# Introduction #

The level editor will allow a user or developer to import or export room designs, as well as provide an interface for modifying tile layouts and arranging entities.

# Revision History #

Kept in a double-ended queue for undoing and redoing. If memory becomes an issue, consider temporarily dumping history into a file, then loading and unloading as necessary.

# Tools #

Things like brushes and selectors.

## Radial Brush ##

Insert tiles up to a specified radius away from the brush. There could be an "outline" mode in which a secondary tile is used to outline the stroke's silhouette.

## Flood Fill ##

Select a tile type and a tile already in the level. The selected tile and all of its connected neighbors of the same type will become the selected type.