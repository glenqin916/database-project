package osu.cse3241;
import java.sql.SQLException;
import java.util.*;


class Main {
    static Scanner sc;
    static Artist artist;
    private static String DATABASE = "Library_Database.db";

    static ArrayList<String> artistList = new ArrayList<>();
    static HashMap<String, Double> trackMap = new HashMap<>();

    public static void search(Artist artist, Actor actor, Director director, Author author, String db) throws SQLException {
        System.out.println("Select the following options by entering the respective number:");
        System.out.println("1. Search for an artist\n2. Search for an actor\n3. Search for a director\n4. Search an author\n5. Search for a album\n6. Search fo a track\n7. Search for a book\n8. Search for a movie\n8. Back");
        sc = new Scanner(System.in);
        int option = sc.nextInt();

        switch(option) {
            case 1:
                //search artist
                artist.getArtist(db);
                break;
            case 2:
                //search actor
            	actor.getActor(db);
                break;
            case 3:
                //search director
            	director.getDirector(db);
                break;
            case 4:
                //search author
            	author.getAuthor(db);
                break;
            case 5:
            	 //search album
            	artist.getAlbum(db);
                break;
            case 6:
           	 	//search track
            	artist.getTrack(db);
               break;
            case 7:
           	 	//search book
            	author.getBook(db);
               break;
            case 8:
           	 	//search movie
            	director.getMovie(db);
               break;
            default:
                break;
        } 
    }

    public static void addNewRecords(Artist artist, Actor actor, Director director, Author author, Content content, People people, String db) throws SQLException {
        System.out.println("Select the following options by entering the respective number:");
        System.out.println("1. Add an artist\n2. Add a actor\n3. Add a director\n4. Add an author\n5. Add a album\n6. Add a track\n7. Add a book\n8. Add a movie\n9. Back");
        sc = new Scanner(System.in);
        int option = sc.nextInt();

        switch(option) {
            case 1:
                //add artist
                artist.addArtist(db, people);
                break;
            case 2:
            	//add actor
            	actor.addActor(db, people);
            	actor.addRole(db,  director);
                break;
            case 3:
            	//add director
            	director.addDirector(db, people);
            case 4:
            	//add author
            	author.addAuthor(db, people);
            case 5:
            	//add album
            	artist.addAlbum(db, content);
            	break;
            case 6:
            	//add track
            	artist.addTrack(db, content);
            	break;
            case 7:
            	//add book
            	author.addBook(db, content);
            	break;
            case 8:
            	//add movie
            	director.addMovie(db, content);
            default:
                break;
        }
    }

    public static void order(Content content, String db) throws SQLException {
        System.out.println("Select the following options by entering the respective number:");
        System.out.println("1. Add orders\n2. Report arrived orders\n3. Back");
        sc = new Scanner(System.in);
        int option = sc.nextInt();

        switch(option) {
            case 1:
                //add order
                content.addOrder(db);
                break;
            case 2:
                //to be implemented later
            	content.transferOrder(db, content);
                break;
            default:
                break;
        } 
    }
    
    public static void managePatron(Patron patron, Artist artist, Author author, Director director, Content content, String db) throws SQLException {
    	System.out.println("Select the following options by entering the respective number:");
        System.out.println("1. Check out an item\n2. Return an item\n3. List all checked out items\n4. Deactivate card\n5. List Patrons\n6. Back");
        sc = new Scanner(System.in);
        int option = sc.nextInt();
        
        switch(option) {
        case 1:
        	//checkout
        	patron.checkout(db, author, artist, director, content);
        	break;
        case 2:
        	//return
        	patron.returnItem(db, content);
        	break;
        case 3:
        	//list
        	patron.listCheckouts(db);
        	break;
        case 4:
        	//deactivate
        	patron.expiredCard(db);
        	break;
        case 5:
        	patron.listPatrons(db);
        	break;
        default:
        	break;
        }
    }

    public static void edit(Actor actor, Artist artist, Director director, Author author, Patron patron, Content content, String db) throws SQLException {
    	System.out.println("Select the following options by entering the respective number: ");
    	System.out.println("1. Edit actor\n2. Edit artist\n3. Edit album\n4. Edit track\n5. Edit director\n6. Edit movie\n7. Edit author\n8. Edit book\n9. Edit Patron\n10. Edit Content\n11. Back");
        sc = new Scanner(System.in);
        int option = sc.nextInt();

        switch(option) {
        case 1:
        	//edit actor
        	actor.editActor(db);
        	break;
        case 2:
        	//edit artist
        	artist.editArtist(db);
        	break;
        case 3:
        	//edit album
        	artist.editAlbum(db);
        	break;
        case 4:
        	//edit track
        	artist.editTrack(db);
        	break;
        case 5:
        	//edit director
        	director.editDirector(db);
        	break;
        case 6:
        	//edit movie
        	director.editMovie(db);
        	break;
        case 7:
        	//edit author
        	author.editAuthor(db);
        	break;
        case 8:
        	//edit book
        	author.editBook(db);
        	break;
        case 9:
        	//edit patrons
        	patron.editPatrons(db);
        	break;
        case 10:
        	//edit content
        	content.editContent(db);
        	break;
        default:
        	break;
        }
    }
    
    public static void delete(Artist artist, Director director, Author author, Content content, String db) throws SQLException {
    	System.out.println("Select the following options by entering the respective number: ");
    	System.out.println("1. Delete album\n2. Delete track\n3. Delete movie\n4. Delete book\n5. Back");
        sc = new Scanner(System.in);
        int option = sc.nextInt();
        
        switch(option) {
        	case 1:
        		//delete album
        		artist.deleteAlbum(db, content);
        		break;
        	case 2:
        		//delete track
        		artist.deleteTrack(db, content);
        		break;
        	case 3:
        		//delete movie
        		director.deleteMovie(db, content);
        		break;
        	case 4:
        		//delete book
        		author.deleteBook(db, content);
        	default:
        		break;
        }	
    }
    
    public static void usefulReports(Patron patron, String db) throws SQLException {
    	System.out.println("Select the following options by entering the respective number: ");
    	System.out.println("1. See overdue books\n2. See all returned items\n3. Back");
        sc = new Scanner(System.in);
        int option = sc.nextInt();
        
        switch(option) {
        	case 1:
        		//overdue
        		patron.overdue(db);
        		break;
        	case 2:
        		//returned
        		patron.listReturn(db);
        	default:
        		break;
        }
    }

    public static void main(String[] args) throws SQLException {
    	Artist artist = new Artist();
    	Actor actor = new Actor();
    	Director director = new Director();
    	Author author = new Author();
    	Patron patron = new Patron();
    	Content content = new Content();
    	People people = new People();
        int quit = 0;
        
        System.out.println("MAIN MENU");
        System.out.println("Select one of the following options by entering the respective number:");
        System.out.println("1. Search\n2. Add new records\n3. Order items\n4. Edit records\n5. Useful reports\n6. Register for a library card\n7. Manage your account\n8. Delete content/people\n9. Exit");
        sc = new Scanner(System.in);

        while(quit != 1) {
            System.out.print("Select an option from the main menu: ");
            int option = sc.nextInt();
        
            switch(option) {
                case 1:
                    //search
                    search(artist, actor, director, author, DATABASE);
                    break;
                case 2:
                    //add new records
                    addNewRecords(artist, actor, director, author, content, people, DATABASE);
                    break;
                case 3:
                    //order items
                    order(content, DATABASE);
                    break;
                case 4:
                    //edit records
                    edit(actor, artist, director, author, patron, content, DATABASE);
                    break;
                case 5:
                    //useful reports
                    usefulReports(patron, DATABASE);
                    break;
                case 6:
                    //register for card
                    patron.addPatron(DATABASE);
                    break;
                case 7:
                    //patron account management
                    managePatron(patron, artist, author, director, content, DATABASE);
                    break;
                case 8:
                	//delete entries
                	delete(artist, director, author, content, DATABASE);
                	break;
                case 9:
                    //exit
                    quit = 1;
                    break;
                default:
                    break;
            }
        }
    }
}