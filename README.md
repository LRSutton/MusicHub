# MusicHub

## Assumptions:

1. Java 8 - Java 8 would be acceptable
1. CSV v. CVS file - Emailed instructions indicated CVS. Assumed that is a typo and intention is CSV
1. Java only - No additional libraries used so output to System.out rather than using logging libraries, etc.
1. Smaller files - Since no indication was made for a "design if this had to be used for large files", I used simple file reads and writes rather than complex buffered approaches
1. UTF-8 Encoding - Data files in and out are UTF-8
1. Didn't want accidental clobbering .. will refuse to write to a file that already exists
1. Simplified running .. Kept to a single file rather than more expressive / organized interfaces, classes, tests, etc.

## Thought Process

For the described challenge a map / reduce approach seemed like the best approach for bringing the relevant data points to light. More iterative steps that would possibly be easier to maintain, especially by more junior staff, would have been feasible but would have come at the cost of performance.

Design consequences traded CPU and a bit of memory for processing time.

If designing for a larger data corpus, I probably would have broken the file reads into a number of buffered reads.

## Build

```
cd <Project root / clone directory>
javac -target 1.8 -source 1.8 *.java -d .
```

## Running

Assuming a successful build from the previous step...
```
java MusicHub <input file> <output file>
```

(Locally, on a MacBook Pro (Mid-2013) the application runs in about 1.4 seconds. (1.7 seconds when using synchronous streams).

## Notes
1. Application will refuse to overwrite existing files

