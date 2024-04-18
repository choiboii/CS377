/*
THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
CODE WRITTEN BY OTHER STUDENTS OR COPIED FROM ONLINE RESOURCES.
ANDREW CHOI
*/

import java.util.ArrayList;
import java.sql.*;
import java.util.Collections;

public class ArtistBrowser {

	/* A connection to the database */
	private Connection connection;

	/**
	 * Constructor loads the JDBC driver. No need to modify this.
	 */
	public ArtistBrowser() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to locate the JDBC driver.");
		}
	}

	/**
	* Establishes a connection to be used for this session, assigning it to
	* the private instance variable 'connection'.
	*
	* @param  url       the url to the database
	* @param  username  the username to connect to the database
	* @param  password  the password to connect to the database
	* @return           true if the connection is successful, false otherwise
	*/
	public boolean connectDB(String url, String username, String password) {
		try {
			this.connection = DriverManager.getConnection(url, username, password);
			return true;
		} catch (SQLException se) {
			System.err.println("SQL Exception: " + se.getMessage());
			return false;
		}
	}

	/**
	* Closes the database connection.
	*
	* @return true if the closing was successful, false otherwise.
	*/
	public boolean disconnectDB() {
		try {
			this.connection.close();
		return true;
		} catch (SQLException se) {
			System.err.println("SQL Exception: " + se.getMessage());
			return false;
		}
	}


	/**
	 * Returns a sorted list of the names of all musicians who were part of a band
	 * at some point between a given start year and an end year (inclusive).
 	 *
	 * Returns an empty list if no musicians match, or if the given timeframe is invalid.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *		Use prepared statements.
	 *
	 * @param startYear
	 * @param endYear
	 * @return  a sorted list of artist names
	 */
	public ArrayList<String> findArtistsInBands(int startYear, int endYear) {
		ArrayList<String> artistsInBands = new ArrayList<String>();
		if(endYear < startYear) {
			return artistsInBands;
		}
		else {
			try{
				//Create prepared statement and execute query
				String queryString = "SELECT * FROM Artist JOIN WasInBand ON Artist.artist_id = WasInBand.artist_id";
				PreparedStatement pStatement = connection.prepareStatement(queryString);
				ResultSet rs = pStatement.executeQuery();

				//traverse through query and choose the correct artists for the given parameters
				while(rs.next()){
					int artistStartYear = rs.getInt("start_year");
					int artistEndYear = rs.getInt("end_year");
					if(!(artistEndYear < startYear || artistStartYear > endYear)){
						String artistName = rs.getString("name");
						//check for duplicates
						if(!artistsInBands.contains(artistName)){
							artistsInBands.add(artistName);
						}
					}
				}
			}
			catch (SQLException se)
			{
					System.err.println("SQL Exception." +
									"<Message>: " + se.getMessage());
			}
		}
		Collections.sort(artistsInBands);
		return artistsInBands;
	}


	/**
	 * Returns a sorted list of the names of all musicians and bands
	 * who released at least one album in a given genre.
	 *
	 * Returns an empty list if no such genre exists or no artist matches.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *		Use prepared statements.
	 *
	 * @param genre  the genre to find artists for
	 * @return       a sorted list of artist names
	 */
	public ArrayList<String> findArtistsInGenre(String genre) {
		ArrayList<String> artistsInGenre = new ArrayList<String>();
		try{
			//Create prepared statement and execute query
			String queryString = "SELECT * FROM Artist JOIN Album ON Artist.artist_id = Album.artist_id JOIN Genre ON Album.genre_id = Genre.genre_id";
			PreparedStatement pStatement = connection.prepareStatement(queryString);
			ResultSet rs = pStatement.executeQuery();

			//traverse through query and choose the correct artists for the given parameters
			while(rs.next()){
				String albumGenre = rs.getString("genre");
				if(albumGenre.equals(genre)){
					String artistName = rs.getString("name");
					//check for duplicates
					if(!artistsInGenre.contains(artistName)){
						artistsInGenre.add(artistName);
					}
				}
			}
		}
		catch (SQLException se)
		{
				System.err.println("SQL Exception." +
								"<Message>: " + se.getMessage());
		}
		Collections.sort(artistsInGenre);
		return artistsInGenre;
	}


	/**
	 * Returns a sorted list of the names of all collaborators
	 * (either as a main artist or guest) for a given artist.
	 *
	 * Returns an empty list if no such artist exists or the artist
	 * has no collaborators.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *		Use prepared statements.
	 *
	 * @param artist  the name of the artist to find collaborators for
	 * @return        a sorted list of artist names
	 */
	public ArrayList<String> findCollaborators(String artist) {
		ArrayList<String> collabWithArtist = new ArrayList<String>();
		try{
			//Create prepared statement and execute query
			String queryString = "SELECT a1.name AS main_name, a2.name AS collab_name " +
			"FROM Collaboration JOIN Artist a1 ON Collaboration.artist1 = a1.artist_id " +
			"JOIN Artist a2 ON Collaboration.artist2 = a2.artist_id";
			PreparedStatement pStatement = connection.prepareStatement(queryString);
			ResultSet rs = pStatement.executeQuery();

			//traverse through query and choose the correct artists for the given parameters
			while(rs.next()){
				String mainArtist = rs.getString("main_name");
				String collabArtist = rs.getString("collab_name");
				if(mainArtist.equals(artist)){
					//check for duplicates
					if(!collabWithArtist.contains(collabArtist)){
						collabWithArtist.add(collabArtist);
					}
				}
				else if(collabArtist.equals(artist)){
					if(!collabWithArtist.contains(mainArtist)){
						collabWithArtist.add(mainArtist);
					}
				}
			}
		}
		catch (SQLException se)
		{
				System.err.println("SQL Exception." +
								"<Message>: " + se.getMessage());
		}
		Collections.sort(collabWithArtist);
		return collabWithArtist;
	}


	/**
	 * Returns a sorted list of the names of all songwriters
	 * who wrote songs for a given artist (the given artist is excluded).
	 *
	 * Returns an empty list if no such artist exists or the artist
	 * has no other songwriters other than themself.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *
	 * @param artist  the name of the artist to find the songwriters for
	 * @return        a sorted list of songwriter names
	 */
	public ArrayList<String> findSongwriters(String artist) {
		ArrayList<String> songwriterNames = new ArrayList<String>();
		try{
			//Create prepared statement and execute query
			String queryString = "SELECT Artist.name AS artist_name, a1.name AS songwriter_name " +
			"FROM Artist JOIN Album ON Artist.artist_id = Album.artist_id " +
			"JOIN BelongsToAlbum ON Album.album_id = BelongsToAlbum.album_id " +
			"JOIN Song ON BelongsToAlbum.song_id = Song.song_id " +
			"JOIN Artist a1 ON Song.songwriter_id = a1.artist_id";
			PreparedStatement pStatement = connection.prepareStatement(queryString);
			ResultSet rs = pStatement.executeQuery();

			//traverse through query and choose the correct artists for the given parameters
			while(rs.next()){
				String artistName = rs.getString("artist_name");
				String songWriterName = rs.getString("songwriter_name");
				if(!artistName.equals(songWriterName) && artistName.equals(artist)){
					//check for duplicates
					if(!songwriterNames.contains(songWriterName)){
						songwriterNames.add(songWriterName);
					}
				}
			}
		}
		catch (SQLException se)
		{
				System.err.println("SQL Exception." +
								"<Message>: " + se.getMessage());
		}
		Collections.sort(songwriterNames);
		return songwriterNames;
	}

	/**
	 * Returns a sorted list of the names of all common acquaintances
	 * for a given pair of artists.
	 *
	 * Returns an empty list if either of the artists does not exist,
	 * or they have no acquaintances.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *
	 * @param artist1  the name of the first artist to find acquaintances for
	 * @param artist2  the name of the second artist to find acquaintances for
	 * @return         a sorted list of artist names
	 */
	public ArrayList<String> findCommonAcquaintances(String artist1, String artist2) {
		ArrayList<String> artist1Acquintances = new ArrayList<String>();
		ArrayList<String> artist2Acquintances = new ArrayList<String>();
		try{
			String queryString = "SELECT a1.name AS main_artist, a2.name AS guest_artist, a3.name AS songwriter " +
			"FROM Song JOIN Collaboration ON Song.song_id = Collaboration.song_id " +
			"JOIN Artist AS a1 ON Collaboration.artist1 = a1.artist_id " +
			"JOIN Artist AS a2 ON Collaboration.artist2 = a2.artist_id "+
			"JOIN Artist AS a3 ON Song.songwriter_id = a3.artist_id;";
			PreparedStatement pStatement = connection.prepareStatement(queryString);
			ResultSet rs = pStatement.executeQuery();

			while(rs.next()){
				//condition for finding all first-layer acquaintances for the two artists
				//artist 1:
				if(rs.getString("main_artist").equals(artist1)){
					if(!artist1Acquintances.contains(rs.getString("guest_artist"))){
						artist1Acquintances.add(rs.getString("guest_artist"));
					}
					if(!artist1Acquintances.contains(rs.getString("songwriter"))){
						artist1Acquintances.add(rs.getString("songwriter"));
					}
				}
				if(rs.getString("guest_artist").equals(artist1)){
					if(!artist1Acquintances.contains(rs.getString("main_artist"))){
						artist1Acquintances.add(rs.getString("main_artist"));
					}
					if(!artist1Acquintances.contains(rs.getString("songwriter"))){
						artist1Acquintances.add(rs.getString("songwriter"));
					}
				}
				if(rs.getString("songwriter").equals(artist1)){
					if(!artist1Acquintances.contains(rs.getString("main_artist"))){
						artist1Acquintances.add(rs.getString("main_artist"));
					}
					if(!artist1Acquintances.contains(rs.getString("guest_artist"))){
						artist1Acquintances.add(rs.getString("guest_artist"));
					}
				}

				//artist 2:
				if(rs.getString("main_artist").equals(artist2)){
					if(!artist2Acquintances.contains(rs.getString("guest_artist"))){
						artist2Acquintances.add(rs.getString("guest_artist"));
					}
					if(!artist2Acquintances.contains(rs.getString("songwriter"))){
						artist2Acquintances.add(rs.getString("songwriter"));
					}
				}
				if(rs.getString("guest_artist").equals(artist2)){
					if(!artist2Acquintances.contains(rs.getString("main_artist"))){
						artist2Acquintances.add(rs.getString("main_artist"));
					}
					if(!artist2Acquintances.contains(rs.getString("songwriter"))){
						artist2Acquintances.add(rs.getString("songwriter"));
					}
				}
				if(rs.getString("songwriter").equals(artist2)){
					if(!artist2Acquintances.contains(rs.getString("main_artist"))){
						artist2Acquintances.add(rs.getString("main_artist"));
					}
					if(!artist2Acquintances.contains(rs.getString("guest_artist"))){
						artist2Acquintances.add(rs.getString("guest_artist"));
					}
				}
			}
		}
		catch (SQLException se)
		{
				System.err.println("SQL Exception." +
								"<Message>: " + se.getMessage());
		}
		artist1Acquintances.retainAll(artist2Acquintances);
		Collections.sort(artist1Acquintances);
		return artist1Acquintances;
	}

	/**
		* Helper method for artistConnectivity:
		* Recursive method that returns true when a connection is made between two artists
		* Otherwise, the arraylist becomes empty of matches and returns false as default.
		*/
	public boolean artistConnect(ArrayList<String[]> artistList, String artist1, String artist2){
		if(artist1.equals(artist2)){
			return true;
		}
		else {
			for(String[] artist : artistList){
				if(artist[0].equals(artist1)){
					String guestArtist = artist[1];
					artistList.remove(artist);
					return artistConnect(artistList, guestArtist, artist2);
				}
				if(artist[1].equals(artist1)){
					String mainArtist = artist[0];
					artistList.remove(artist);
					return artistConnect(artistList, mainArtist, artist2);
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if two artists have a collaboration path connecting
	 * them in the database (see A3 handout for our definition of a path).
	 * For example, artists `Z' and `Usher' are considered connected even though
	 * they have not collaborated directly on any song, because 'Z' collaborated
	 * with `Alicia Keys' who in turn had collaborated with `Usher', therefore there
	 * is a collaboration path connecting `Z' and `Usher'.
	 *
	 * Returns false if there is no collaboration path at all between artist1 and artist2
	 * or if either of them do not exist in the database.
	 *
	 * @return    true iff artist1 and artist2 have a collaboration path connecting them
	 */
	public boolean artistConnectivity(String artist1, String artist2) {
		//four boolean variables:
		//connected: determines if there is a connection
		boolean connected = false;
		//exists variables determine whether the two artists both exist in the database.
		boolean exists = false;
		boolean artist1Exists = false;
		boolean artist2Exists = false;
		ArrayList<String[]> artistsCollab = new ArrayList<String[]>();
		try{
			String queryString = "SELECT a1.name AS main_artist, a2.name AS guest_artist " +
			"FROM Song JOIN Collaboration ON Song.song_id = Collaboration.song_id " +
			"JOIN Artist AS a1 ON Collaboration.artist1 = a1.artist_id " +
			"JOIN Artist AS a2 ON Collaboration.artist2 = a2.artist_id;";
			PreparedStatement pStatement = connection.prepareStatement(queryString);
			ResultSet rs = pStatement.executeQuery();

			//add all the queries resulted into an arraylist
			while(rs.next()){
				String[] artistPair = {rs.getString("main_artist"), rs.getString("guest_artist")};
				artistsCollab.add(artistPair);
			}
		}
		catch (SQLException se)
		{
				System.err.println("SQL Exception." +
								"<Message>: " + se.getMessage());
		}

		//condition for checking if the two artists both exist in database
		for(String[] artist : artistsCollab){
			if(artist[0].equals(artist1) || artist[1].equals(artist1)){
				artist1Exists = true;
			}

			if(artist[0].equals(artist2) || artist[1].equals(artist2)){
				artist2Exists = true;
			}
		}
		//simple boolean logic: makes sure both artists exist, and both the conditions
		//for existing and connecting are satisfied.
		exists = artist1Exists && artist2Exists;
		connected = artistConnect(artistsCollab, artist1, artist2);

		return connected && exists;
	}


	public static void main(String[] args) {

		if( args.length < 2 ){
			System.out.println("Usage: java ArtistBrowser <userName> <password>");
			return;
		}

		String user = args[0];
		String pass = args[1];

		ArtistBrowser a3 = new ArtistBrowser();

		String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=artistDB";
		a3.connectDB(url, user, pass);

		System.err.println("\n----- ArtistsInBands -----");
    ArrayList<String> res = a3.findArtistsInBands(1990,1999);
    for (String s : res) {
      System.err.println(s);
    }

		System.err.println("\n----- ArtistsInGenre -----");
    res = a3.findArtistsInGenre("Rock");
    for (String s : res) {
      System.err.println(s);
    }

		System.err.println("\n----- Collaborators -----");
		res = a3.findCollaborators("Usher");
		for (String s : res) {
		  System.err.println(s);
		}

		System.err.println("\n----- Songwriters -----");
	        res = a3.findSongwriters("Justin Bieber");
		for (String s : res) {
		  System.err.println(s);
		}

		System.err.println("\n----- Common Acquaintances -----");
		res = a3.findCommonAcquaintances("Jaden Smith", "Miley Cyrus");
		for (String s : res) {
		  System.err.println(s);
		}

		System.err.println("\n----- artistConnectivity -----");
		String a1 = "Z", a2 = "Usher";
		boolean areConnected = a3.artistConnectivity(a1, a2);
		System.err.println("Do artists " + a1 + " and " + a2 + " have a collaboration path connecting them? Answer: " + areConnected);

		a3.disconnectDB();
	}
}
