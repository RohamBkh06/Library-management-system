package ir.ac.kntu.util;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.*;

import java.util.List;


public class TestDataSeeder {

    public static void seedTestData() {

        LibraryManger manager = LibraryManger.getInstance();

        // =====================================================
        // ADMINS - 5
        // =====================================================

        manager.addAdmin(new Admin(
                "Parsa",
                "Abdollahi",
                "Parsa_admin",
                "1386",
                Admin.NULL_ADMIN
        ));

        manager.addAdmin(new Admin(
                "Mina",
                "Karimi",
                "Mina_admin",
                "Admin@2024",
                LibraryManger.getInstance().getAdminByPassword().get("1386")
        ));

        manager.addAdmin(new Admin(
                "Sina",
                "Ahmadi",
                "Sina_admin",
                "Sina@2024",
                LibraryManger.getInstance().getAdminByPassword().get("1386")
        ));

        manager.addAdmin(new Admin(
                "Niloofar",
                "Hosseini",
                "Niloofar_admin",
                "Niloofar@2024",
                LibraryManger.getInstance().getAdminByPassword().get("Admin@2024")
        ));

        manager.addAdmin(new Admin(
                "Arman",
                "Rahimi",
                "Arman_admin",
                "Arman@2024",
                LibraryManger.getInstance().getAdminByPassword().get("Niloofar@2024")
        ));


        // =====================================================
        // STUDENTS - 10
        // =====================================================



        manager.addUser(new Student(
                "Sara",
                "Mohammadi",
                "STU-234567",
                "sara.mohammadi@gmail.com",
                "09121234567",
                "Sara@1234"
        ));

        manager.addUser(new Student(
                "Mohammad",
                "Rezaei",
                "STU-345678",
                "m.rezaei@gmail.com",
                "09134567890",
                "Mohammad@1234"
        ));

        manager.addUser(new Student(
                "Negin",
                "Ebrahimi",
                "STU-456789",
                "negin.ebrahimi@gmail.com",
                "09126789012",
                "Negin@1234"
        ));

        manager.addUser(new Student(
                "Amir",
                "Hosseini",
                "STU-567890",
                "amir.hosseini@gmail.com",
                "09127890123",
                "Amir@1234"
        ));

        manager.addUser(new Student(
                "Yasmin",
                "Karimi",
                "STU-678901",
                "yasmin.karimi@gmail.com",
                "09128901234",
                "Yasmin@1234"
        ));

        manager.addUser(new Student(
                "Saeed",
                "Rahimi",
                "STU-789012",
                "saeed.rahimi@gmail.com",
                "09129012345",
                "Saeed@1234"
        ));

        manager.addUser(new Student(
                "Hana",
                "Moradi",
                "STU-890123",
                "hana.moradi@gmail.com",
                "09120123456",
                "Hana@1234"
        ));

        manager.addUser(new Student(
                "Pouya",
                "Jafari",
                "STU-901234",
                "pouya.jafari@gmail.com",
                "09131234567",
                "Pouya@1234"
        ));

        manager.addUser(new Student(
                "Elina",
                "Nouri",
                "STU-012345",
                "elina.nouri@gmail.com",
                "09132345678",
                "Elina@1234"
        ));


        // =====================================================
        // FACULTY - 5
        // =====================================================

        manager.addUser(new Faculty(
                "Reza",
                "Karimi",
                "FAC-124578",
                "r.karimi@university.edu",
                "09125556677",
                "Prof@123"
        ));

        manager.addUser(new Faculty(
                "Mehdi",
                "Ahmadi",
                "FAC-235689",
                "m.ahmadi@university.edu",
                "09123334455",
                "Prof@456"
        ));

        manager.addUser(new Faculty(
                "Leila",
                "Hosseini",
                "FAC-346790",
                "l.hosseini@university.edu",
                "09124445566",
                "Prof@789"
        ));

        manager.addUser(new Faculty(
                "Farhad",
                "Rahimi",
                "FAC-457801",
                "f.rahimi@university.edu",
                "09126667788",
                "Prof@321"
        ));

        manager.addUser(new Faculty(
                "Maryam",
                "Ebrahimi",
                "FAC-568912",
                "m.ebrahimi@university.edu",
                "09127778899",
                "Prof@654"
        ));


        // =====================================================
        // GUESTS - 20
        // =====================================================

        manager.addUser(new Guest(
                "Roham", "Bakhtiari", "GST-125690",
                "roham.bkh@gmail.com", "09123456789", "Guest@123"
        ));

        manager.addUser(new Guest(
                "Kian", "Shahriari", "GST-234781",
                "kian.sh@gmail.com", "09121112233", "Guest@234"
        ));

        manager.addUser(new Guest(
                "Arian", "Moradi", "GST-345892",
                "arian.moradi@gmail.com", "09122223344", "Guest@345"
        ));

        manager.addUser(new Guest(
                "Shayan", "Nouri", "GST-456903",
                "shayan.nouri@gmail.com", "09123334455", "Guest@456"
        ));

        manager.addUser(new Guest(
                "Sahar", "Jafari", "GST-567014",
                "sahar.jafari@gmail.com", "09124445566", "Guest@567"
        ));

        manager.addUser(new Guest(
                "Mahan", "Sadeghi", "GST-678125",
                "mahan.sadeghi@gmail.com", "09125556677", "Guest@678"
        ));

        manager.addUser(new Guest(
                "Roya", "Hashemi", "GST-789236",
                "roya.hashemi@gmail.com", "09126667788", "Guest@789"
        ));

        manager.addUser(new Guest(
                "Arash", "Niknam", "GST-890348",
                "arash.niknam@gmail.com", "09127778899", "Guest@890"
        ));

        manager.addUser(new Guest(
                "Parisa", "Mousavi", "GST-901454",
                "parisa.mousavi@gmail.com", "09128889900", "Guest@901"
        ));

        manager.addUser(new Guest(
                "Kasra", "Yazdani", "GST-012565",
                "kasra.yazdani@gmail.com", "09129990011", "Guest@012"
        ));

        manager.addUser(new Guest(
                "Tara", "Zarei", "GST-123670",
                "tara.zarei@gmail.com", "09121001122", "Guest@123"
        ));



        manager.addUser(new Guest(
                "Pouneh", "Taheri", "GST-890347",
                "pouneh.taheri@gmail.com", "09128778899", "Guest@890"
        ));

        manager.addUser(new Guest(
                "Yasin", "Abbasi", "GST-901458",
                "yasin.abbasi@gmail.com", "09129889900", "Guest@901"
        ));

        manager.addUser(new Guest(
                "Radin", "Mansouri", "GST-012569",
                "radin.mansouri@gmail.com", "09120990011", "Guest@012"
        ));


        // =====================================================
        // SUPPORTERS - 5
        // =====================================================

        Supporter aliSupporter = new Supporter(
                "Ali",
                "Ahmadi",
                "ali_support",
                "123456",
                List.of(
                        Department.FINANCIAL_AFFAIRS,
                        Department.REPORT_PROBLEM
                )
        );

        Supporter saraSupporter = new Supporter(
                "Sara",
                "Mohammadi",
                "sara_support",
                "234567",
                List.of(
                        Department.REQUEST_ITEM,
                        Department.RESERVE_ITEM
                )
        );

        Supporter rezaSupporter = new Supporter(
                "Reza",
                "Karimi",
                "reza_support",
                "345678",
                List.of(
                        Department.REPORT_PROBLEM,
                        Department.RESERVE_ITEM
                )
        );

        Supporter minaSupporter = new Supporter(
                "Mina",
                "Hosseini",
                "mina_support",
                "456789",
                List.of(
                        Department.FINANCIAL_AFFAIRS,
                        Department.REQUEST_ITEM
                )
        );

        Supporter armanSupporter = new Supporter(
                "Arman",
                "Rahimi",
                "arman_support",
                "567890",
                List.of(
                        Department.REQUEST_ITEM,
                        Department.REPORT_PROBLEM,
                        Department.RESERVE_ITEM
                )
        );

        manager.addSupporter(aliSupporter);
        manager.addSupporter(saraSupporter);
        manager.addSupporter(rezaSupporter);
        manager.addSupporter(minaSupporter);
        manager.addSupporter(armanSupporter);


        // =====================================================
        // BOOKS - 15
        // =====================================================

        aliSupporter.addItem(new Book(
                "Clean Code",
                "BOK-12453678",
                2008,
                "Programming",
                "Robert C. Martin",
                464,
                5,
                "9780132350884"
        ));

        aliSupporter.addItem(new Book(
                "Introduction to Algorithms",
                "BOK-12345678",
                2022,
                "Computer Science",
                "Thomas H. Cormen",
                1312,
                2,
                "9780262046305"
        ));

        aliSupporter.addItem(new Book(
                "Effective Java",
                "BOK-23456789",
                2018,
                "Programming",
                "Joshua Bloch",
                416,
                4,
                "9780134685991"
        ));

        saraSupporter.addItem(new Book(
                "The Pragmatic Programmer",
                "BOK-34567890",
                2019,
                "Programming",
                "David Thomas",
                352,
                3,
                "9780135957059"
        ));

        saraSupporter.addItem(new Book(
                "Design Patterns",
                "BOK-45678901",
                1994,
                "Computer Science",
                "Erich Gamma",
                395,
                2,
                "9780201633610"
        ));

        rezaSupporter.addItem(new Book(
                "Java Concurrency in Practice",
                "BOK-56789012",
                2006,
                "Programming",
                "Brian Goetz",
                432,
                3,
                "9780321349606"
        ));

        rezaSupporter.addItem(new Book(
                "Artificial Intelligence: A Modern Approach",
                "BOK-67890123",
                2020,
                "Artificial Intelligence",
                "Stuart Russell",
                1136,
                2,
                "9780134610993"
        ));

        minaSupporter.addItem(new Book(
                "The Selfish Gene",
                "BOK-78901234",
                2006,
                "Science",
                "Richard Dawkins",
                384,
                4,
                "9780199291151"
        ));

        minaSupporter.addItem(new Book(
                "A Brief History of Time",
                "BOK-89012345",
                1990,
                "Science",
                "Stephen Hawking",
                256,
                3,
                "9780553380163"
        ));

        armanSupporter.addItem(new Book(
                "Thinking, Fast and Slow",
                "BOK-90123456",
                2011,
                "Psychology",
                "Daniel Kahneman",
                499,
                2,
                "9780374533557"
        ));

        armanSupporter.addItem(new Book(
                "The Psychology of Money",
                "BOK-01234567",
                2020,
                "Business",
                "Morgan Housel",
                256,
                5,
                "9780857197689"
        ));

        aliSupporter.addItem(new Book(
                "The Great Gatsby",
                "BOK-11223344",
                1925,
                "Literature",
                "F. Scott Fitzgerald",
                180,
                3,
                "9780743273565"
        ));

        saraSupporter.addItem(new Book(
                "1984",
                "BOK-22334455",
                1949,
                "Literature",
                "George Orwell",
                328,
                4,
                "9780451524935"
        ));

        rezaSupporter.addItem(new Book(
                "Sapiens",
                "BOK-33445566",
                2015,
                "History",
                "Yuval Noah Harari",
                443,
                3,
                "9780062316097"
        ));

        minaSupporter.addItem(new Book(
                "The Innovators",
                "BOK-44556677",
                2014,
                "Technology",
                "Walter Isaacson",
                560,
                2,
                "9781476708706"
        ));


        // =====================================================
        // MAGAZINES - 15
        // =====================================================

        aliSupporter.addItem(new Magazine(
                "Time",
                "MAG-11111111",
                2025,
                "News",
                12,
                "0040-781X",
                PublicationFrequency.WEEKLY
        ));

        aliSupporter.addItem(new Magazine(
                "Scientific American",
                "MAG-22222222",
                2025,
                "Science",
                6,
                "0036-8733",
                PublicationFrequency.MONTHLY
        ));

        aliSupporter.addItem(new Magazine(
                "Wired",
                "MAG-33333333",
                2025,
                "Technology",
                8,
                "1059-1028",
                PublicationFrequency.MONTHLY
        ));

        saraSupporter.addItem(new Magazine(
                "The Economist",
                "MAG-44444444",
                2025,
                "Business",
                10,
                "0013-0613",
                PublicationFrequency.WEEKLY
        ));

        saraSupporter.addItem(new Magazine(
                "National Geographic",
                "MAG-55555555",
                2025,
                "Science",
                4,
                "0027-9358",
                PublicationFrequency.MONTHLY
        ));

        rezaSupporter.addItem(new Magazine(
                "MIT Technology Review",
                "MAG-66666666",
                2025,
                "Technology",
                3,
                "0040-169X",
                PublicationFrequency.MONTHLY
        ));

        rezaSupporter.addItem(new Magazine(
                "IEEE Spectrum",
                "MAG-77777777",
                2025,
                "Computer Science",
                5,
                "0018-9235",
                PublicationFrequency.MONTHLY
        ));

        minaSupporter.addItem(new Magazine(
                "Forbes",
                "MAG-88888888",
                2025,
                "Business",
                9,
                "0015-6914",
                PublicationFrequency.MONTHLY
        ));

        minaSupporter.addItem(new Magazine(
                "Psychology Today",
                "MAG-99999999",
                2025,
                "Psychology",
                6,
                "0033-2747",
                PublicationFrequency.MONTHLY
        ));

        armanSupporter.addItem(new Magazine(
                "The Atlantic",
                "MAG-10101010",
                2025,
                "Literature",
                5,
                "1072-7825",
                PublicationFrequency.MONTHLY
        ));

        armanSupporter.addItem(new Magazine(
                "History Today",
                "MAG-12121212",
                2025,
                "History",
                7,
                "0018-2753",
                PublicationFrequency.MONTHLY
        ));

        aliSupporter.addItem(new Magazine(
                "PCMag",
                "MAG-13131313",
                2025,
                "Programming",
                4,
                "0888-8507",
                PublicationFrequency.MONTHLY
        ));

        saraSupporter.addItem(new Magazine(
                "Communications of the ACM",
                "MAG-14141414",
                2025,
                "Computer Science",
                3,
                "0001-0782",
                PublicationFrequency.MONTHLY
        ));

        rezaSupporter.addItem(new Magazine(
                "Nature",
                "MAG-15151515",
                2025,
                "Science",
                2,
                "0028-0836",
                PublicationFrequency.WEEKLY
        ));

        minaSupporter.addItem(new Magazine(
                "Harvard Business Review",
                "MAG-16161616",
                2025,
                "Business",
                6,
                "0017-8012",
                PublicationFrequency.MONTHLY
        ));


        // =====================================================
        // EBOOKS - 15
        // =====================================================

        aliSupporter.addItem(new Ebook(
                "Head First Java",
                "EBK-11111111",
                2022,
                "Programming",
                DigitalFormat.PDF,
                18.5,
                "https://library.local/ebooks/head-first-java.pdf",
                720
        ));

        aliSupporter.addItem(new Ebook(
                "Python Crash Course",
                "EBK-22222222",
                2023,
                "Programming",
                DigitalFormat.PDF,
                15.0,
                "https://library.local/ebooks/python-crash-course.pdf",
                544
        ));

        aliSupporter.addItem(new Ebook(
                "Computer Networking",
                "EBK-33333333",
                2021,
                "Computer Science",
                DigitalFormat.PDF,
                22.5,
                "https://library.local/ebooks/computer-networking.pdf",
                960
        ));

        saraSupporter.addItem(new Ebook(
                "The Future of Technology",
                "EBK-44444444",
                2024,
                "Technology",
                DigitalFormat.EPUB,
                12.0,
                "https://library.local/ebooks/future-of-technology.epub",
                320
        ));

        saraSupporter.addItem(new Ebook(
                "The Science of Everything",
                "EBK-55555555",
                2022,
                "Science",
                DigitalFormat.PDF,
                14.5,
                "https://library.local/ebooks/science-everything.pdf",
                410
        ));

        rezaSupporter.addItem(new Ebook(
                "Artificial Intelligence Basics",
                "EBK-66666666",
                2024,
                "Artificial Intelligence",
                DigitalFormat.PDF,
                19.0,
                "https://library.local/ebooks/ai-basics.pdf",
                600
        ));

        rezaSupporter.addItem(new Ebook(
                "Modern Software Architecture",
                "EBK-77777777",
                2023,
                "Programming",
                DigitalFormat.EPUB,
                17.5,
                "https://library.local/ebooks/software-architecture.epub",
                520
        ));

        minaSupporter.addItem(new Ebook(
                "Understanding Psychology",
                "EBK-88888888",
                2020,
                "Psychology",
                DigitalFormat.PDF,
                13.0,
                "https://library.local/ebooks/psychology.pdf",
                380
        ));

        minaSupporter.addItem(new Ebook(
                "World History",
                "EBK-99999999",
                2021,
                "History",
                DigitalFormat.PDF,
                16.5,
                "https://library.local/ebooks/world-history.pdf",
                700
        ));

        armanSupporter.addItem(new Ebook(
                "Principles of Economics",
                "EBK-10101010",
                2022,
                "Business",
                DigitalFormat.EPUB,
                14.0,
                "https://library.local/ebooks/economics.epub",
                450
        ));

        armanSupporter.addItem(new Ebook(
                "Modern Literature",
                "EBK-12121212",
                2020,
                "Literature",
                DigitalFormat.PDF,
                11.5,
                "https://library.local/ebooks/modern-literature.pdf",
                290
        ));

        aliSupporter.addItem(new Ebook(
                "Data Structures and Algorithms",
                "EBK-13131313",
                2023,
                "Computer Science",
                DigitalFormat.PDF,
                21.0,
                "https://library.local/ebooks/data-structures.pdf",
                800
        ));

        saraSupporter.addItem(new Ebook(
                "Java Programming",
                "EBK-14141414",
                2024,
                "Programming",
                DigitalFormat.EPUB,
                18.0,
                "https://library.local/ebooks/java-programming.epub",
                650
        ));

        rezaSupporter.addItem(new Ebook(
                "Digital Transformation",
                "EBK-15151515",
                2023,
                "Technology",
                DigitalFormat.PDF,
                15.5,
                "https://library.local/ebooks/digital-transformation.pdf",
                480
        ));

        minaSupporter.addItem(new Ebook(
                "The Human Mind",
                "EBK-16161616",
                2021,
                "Psychology",
                DigitalFormat.PDF,
                12.5,
                "https://library.local/ebooks/human-mind.pdf",
                350
        ));


        // =====================================================
        // AUDIOBOOKS - 15
        // =====================================================

        aliSupporter.addItem(new AudioBook(
                "The Cosmos Explained",
                "AUD-11111111",
                2023,
                "Science",
                DigitalFormat.MP3,
                540.0,
                "https://library.local/audio/cosmos.mp3",
                390
        ));

        aliSupporter.addItem(new AudioBook(
                "Clean Code Audio Edition",
                "AUD-22222222",
                2022,
                "Programming",
                DigitalFormat.MP3,
                420.0,
                "https://library.local/audio/clean-code.mp3",
                464
        ));

        aliSupporter.addItem(new AudioBook(
                "The Pragmatic Programmer",
                "AUD-33333333",
                2021,
                "Programming",
                DigitalFormat.MP3,
                480.0,
                "https://library.local/audio/pragmatic-programmer.mp3",
                352
        ));

        saraSupporter.addItem(new AudioBook(
                "A Brief History of Humanity",
                "AUD-44444444",
                2020,
                "History",
                DigitalFormat.MP3,
                600.0,
                "https://library.local/audio/humanity.mp3",
                520
        ));

        saraSupporter.addItem(new AudioBook(
                "The World of Science",
                "AUD-55555555",
                2022,
                "Science",
                DigitalFormat.MP3,
                360.0,
                "https://library.local/audio/world-science.mp3",
                300
        ));

        rezaSupporter.addItem(new AudioBook(
                "Artificial Intelligence Today",
                "AUD-66666666",
                2024,
                "Artificial Intelligence",
                DigitalFormat.MP3,
                420.0,
                "https://library.local/audio/ai-today.mp3",
                400
        ));

        rezaSupporter.addItem(new AudioBook(
                "The Software Developer's Guide",
                "AUD-77777777",
                2023,
                "Computer Science",
                DigitalFormat.MP3,
                510.0,
                "https://library.local/audio/software-guide.mp3",
                450
        ));

        minaSupporter.addItem(new AudioBook(
                "The Psychology of Success",
                "AUD-88888888",
                2021,
                "Psychology",
                DigitalFormat.MP3,
                390.0,
                "https://library.local/audio/psychology-success.mp3",
                340
        ));

        minaSupporter.addItem(new AudioBook(
                "Money and Business",
                "AUD-99999999",
                2022,
                "Business",
                DigitalFormat.MP3,
                450.0,
                "https://library.local/audio/money-business.mp3",
                380
        ));

        armanSupporter.addItem(new AudioBook(
                "The Power of Innovation",
                "AUD-10101010",
                2023,
                "Technology",
                DigitalFormat.MP3,
                420.0,
                "https://library.local/audio/innovation.mp3",
                360
        ));

        armanSupporter.addItem(new AudioBook(
                "Great Works of Literature",
                "AUD-12121212",
                2020,
                "Literature",
                DigitalFormat.MP3,
                720.0,
                "https://library.local/audio/literature.mp3",
                600
        ));

        aliSupporter.addItem(new AudioBook(
                "Algorithms Made Simple",
                "AUD-13131313",
                2022,
                "Computer Science",
                DigitalFormat.MP3,
                480.0,
                "https://library.local/audio/algorithms.mp3",
                500
        ));

        saraSupporter.addItem(new AudioBook(
                "Java in Practice",
                "AUD-14141414",
                2023,
                "Programming",
                DigitalFormat.MP3,
                540.0,
                "https://library.local/audio/java-practice.mp3",
                450
        ));

        rezaSupporter.addItem(new AudioBook(
                "The Digital Age",
                "AUD-15151515",
                2024,
                "Technology",
                DigitalFormat.MP3,
                360.0,
                "https://library.local/audio/digital-age.mp3",
                300
        ));

        minaSupporter.addItem(new AudioBook(
                "Understanding Human Behavior",
                "AUD-16161616",
                2021,
                "Psychology",
                DigitalFormat.MP3,
                420.0,
                "https://library.local/audio/human-behavior.mp3",
                370
        ));
    }
}

