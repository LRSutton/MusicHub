# MusicHub

## Assumptions:

1. Java 8 - Java 8 would be acceptable / preferable over earlier versions
1. CSV v. CVS file - Emailed instructions indicated CVS. Assumed that is a typo and intention is CSV
1. Java only - No additional libraries used so output to System.out rather than using logging libraries, etc.
1. Smaller files - Since no indication was made for a "design if this had to be used for large files", I used simple file reads and writes rather than complex buffered approaches
1. UTF-8 Encoding - Data files in and out are UTF-8
1. Didn't want accidental clobbering .. will refuse to write to a file that already exists

## Thought Process

## Build

## Running
java -jar musichub.jar <input file> <output file>
