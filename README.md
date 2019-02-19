# CellCounter
To use: add a "Outputs" and a "Images" folder
put all images in the Images folder, and then run.
ImageData is a csv that contains the count of cells next to the file name.
Outputs are the image outputs 2 per image
Orig prefix are what the program sees.
Seen prefix is all cells that were counted highlighted in blue.

# Notes
ignores .DS_Store in Images, untested for Windows equivalent.
+/- 2 cells is possible. This program has been fine tuned for small 10 pixel to 100 pixel cells
The cells need to be dark on a light background, if the background is dark consider changing threshold in ImageTester Class
If cells are tightly clumped it will not read them, consider changing the round value in ImageTester called in main, however this will decrease the ability to ignore noise and increase false positives

