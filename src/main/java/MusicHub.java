import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main application to run
 */
public class MusicHub {
	final private static Charset ENCODING = Charset.forName("UTF-8");
	private final Map<Integer,String> pairs = new HashMap<Integer,String>();

	/**
	 *
	 * @param args
	 * @throws InvalidUsageException - Thrown if app usage is not adhered to
	 * @throws IOException - Thrown if data file is not found or if it has a parsing error.
	 */
	public static void main (final String[] args) throws InvalidUsageException, IOException {
		if (args.length != 2) {
			throw new InvalidUsageException("Usage: ./gradlew <Full Path to Input Data File> <Full Path to Output Data File>");
		}
		final Path inputPath = Paths.get(args[0]);
		final Path outputPath = Paths.get(args[1]);

		final MusicHub hub = new MusicHub();

		// Read entire data file in one step
		final List<String> fileLines = readFile(inputPath);

		// Capture start time for simple benchmarking to ensure its somewhat performant
		final Instant iStart = Instant.now();

		// Stretching my legs with Java 8 streams...
		final List<String> results =
				// Convert read lines from file to stream
				fileLines.stream()
				// Split each line into separate artists
				.map(l -> l.split(","))
				// Call method to convert artist groups into tuples of artist pairs
				.map(hub::parseToTuples)
				// Collapse the large list of lists of tuples into a single stream
				.flatMap(l -> l.stream())
				// Count frequency of each tuple hash (produces map)
				.collect(Collectors.groupingBy(e -> e, Collectors.counting()))
				// Stream the map's entry set
				.entrySet().stream()
				// Sort by frequency (most to least)
				.sorted((e1,e2) -> e2.getValue().compareTo(e1.getValue()))
				// Filter out those that didn't show up at least 50 times
				.filter(e -> e.getValue() >= 50)
				// Convert output to human readable artist pairs instead of tuple hash
				.map(e -> {
					final String artists = hub.pairs.get(e.getKey());
					return String.format("%s,%d", artists,e.getValue());
				})
				// Collect into our complete list of artist pairs that appear 50 or more times
				.collect(Collectors.toList());

		// Stop timing
		final Instant iEnd = Instant.now();
		// Braggart statement
		System.out.format("Processing Runtime (excludes file reads and writes): %d millis%n%n", ChronoUnit.MILLIS.between(iStart,iEnd));

		// Output CSV file
		writeFile(outputPath,results);
	}

	/*
	 * Function for converting 2 artists to a hash ID (idempotent regardless of artist ordering)
	 */
	public Hasher<String> setHash = (s1,s2) -> new HashSet<String>(Arrays.asList(s1,s2)).hashCode();

	/*
	 * Convert an array of artist names into a list of integer-based tuples
	 */
	public List<Integer> parseToTuples (final String[] artists) {
		final List<Integer> tuples = new ArrayList<Integer>();
		int loopIndex = 0;

		while (loopIndex < artists.length) {
			int tupleIndex = loopIndex + 1;
			final String artist = artists[loopIndex];
			while (tupleIndex < artists.length) {
				final String other = artists[tupleIndex];
				final int hash = setHash.hash(artist, other);
				if (!pairs.containsKey(hash)) {
					pairs.put(hash, String.format("%s,%s", artist,other));
				}
				tuples.add(hash);
				tupleIndex++;
			}
			loopIndex++;
		}

		return tuples;
	};

	/*
	 * Read file from file system
	 */
	private static List<String> readFile(final Path path) throws IOException {
		if (!Files.exists(path)) {
			throw new FileNotFoundException("Unable to find data file: " + path.toString());
		}
		return Files.readAllLines(path, MusicHub.ENCODING);
	}

	/*
	 * Write file to file system
	 */
	private static void writeFile(final Path path, final List<String> data) {
		if (Files.exists(path)) {
			System.out.println("Cowardly refusing to overwrite " + path.toString());
			return;
		}

		final Stream<String> dataStream = data.stream();
		try (PrintWriter pw = new PrintWriter(path.toString(), MusicHub.ENCODING.toString())) {
			dataStream.forEach(pw::println);
		} catch (final IOException ex) {
			System.out.format("Unable to write output file: %s", ex.getMessage());
		}
	}
}
