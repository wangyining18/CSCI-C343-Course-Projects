#### DNA Sequence Alignment

<img src="sequence-alignment-screenshot-1" alt="sequence-alignment-screenshot" width="400" />

##### Compilation

```
cd src/
javac *
```

##### Run
```
java Driver
```



##### Implementation

To update the score of the current R[x][y], the new score can come from three possible positions that are next to R[x][y]: up, left, and upleft corner. For up, the new score should be subtracted one from the score of the up. For left, the new score should be subtracted one from the value in the left box. For the upleft corner, it depends on weather the two letters match or not. If they match, the new score will be the upleft corner score plus two  and if they don't match, the new score will be the upleft corner score minus two. The largest number from the three options will be used to update. The chosen position will be recorded as I(left), D(up) or M(upleft corner). Each of the changes will be in a for loop that starts from the left to right for each row, and then go down to the next row, until the entire matrix has been updated. The first row and the first column will be a for loop to decrease the value by 1 starting at 0, from left to right, and up to down, respectively. 


For composing s1_align and s2_align, back-tracing from the most right bottom corner. If the current cell is M, s1_align appends s1's letter at this position and s2_aligh appends s2's letter at the position and the position moves one cell to the upleft corner. If the current cell is I, s1_align appends "_" and s2_align appends s2's letter and the position moves one cell up. If the current cell is D, s1_align appends the current s1 letter and s2_align appends "_" and the position moves one cell to the left. Repeat the process until reach the up left corner of entire matrix and find the track of a list of positions. 

