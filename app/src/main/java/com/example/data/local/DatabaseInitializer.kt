package com.example.data.local

import com.example.data.model.Category
import com.example.data.model.Difficulty
import com.example.data.model.LevelConfig
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.data.model.UserProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DatabaseInitializer {

    val defaultCategories = listOf(
        Category("countries", "Countries", "🌎", "Flags, capitals & world geography", 1, true),
        Category("cars", "Cars", "🚗", "Supercars, badges, makers & speed", 2, true),
        Category("phones", "Phones", "📱", "Flagships, cameras, operating systems & features", 3, true),
        Category("tech", "Technology", "💻", "Hardware, AI, startups & computing", 4, true),
        Category("food", "Food", "🍔", "Delicious dishes, culinary origins & snacks", 5, true),
        Category("movies", "Movies", "🎬", "Blockbusters, actors, directors & quotes", 6, true),
        Category("football", "Football", "⚽", "Legends, clubs, trophies & World Cups", 7, true),
        Category("brands", "Brands & Logos", "🏢", "Famous trademarks, slogans & logos", 8, true),
        Category("animals", "Animals", "🐾", "Wildlife, habitats & animal records", 9, true),
        Category("music", "Music", "🎵", "Artists, top hits, genres & instruments", 10, true),
        Category("fashion", "Fashion", "👕", "Designer houses, streetwear & iconic styles", 11, true),
        Category("places", "Famous Places", "🏛️", "Wonders, monuments & historic landmarks", 12, true),
        Category("ethiopia", "Ethiopia", "🇪🇹", "History, culture, landmarks, coffee & icons", 13, true),
        Category("general", "General Knowledge", "🧠", "Brain-teasing facts and trivia", 14, true),
        Category("fun", "Fun & Random", "😂", "Whimsical riddles and pop-culture puns", 15, true)
    )

    val defaultLevels = listOf(
        LevelConfig(1, "Beginner", 0),
        LevelConfig(2, "Curious", 500),
        LevelConfig(3, "Smart", 1500),
        LevelConfig(4, "Expert", 3500),
        LevelConfig(5, "Master", 7000)
    )

    fun getInitialQuestions(): List<Question> = listOf(
        // COUNTRIES
        Question(
            categoryId = "countries",
            questionText = "Which country does this flag belong to?",
            questionType = QuestionType.FLAG,
            visualClue = "🇯🇵",
            answer = "Japan",
            optionA = "China",
            optionB = "Japan",
            optionC = "South Korea",
            optionD = "Thailand",
            explanation = "The flag of Japan is known as the Hinomaru (circle of the sun).",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "countries",
            questionText = "Which country does this iconic flag represent?",
            questionType = QuestionType.FLAG,
            visualClue = "🇧🇷",
            answer = "Brazil",
            optionA = "Argentina",
            optionB = "Colombia",
            optionC = "Brazil",
            optionD = "Portugal",
            explanation = "Brazil's green and yellow flag features a blue celestial globe.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "countries",
            questionText = "Identify this European country from its flag:",
            questionType = QuestionType.FLAG,
            visualClue = "🇮🇹",
            answer = "Italy",
            optionA = "France",
            optionB = "Ireland",
            optionC = "Mexico",
            optionD = "Italy",
            explanation = "The Italian tricolour features vertical green, white, and red bands.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "countries",
            questionText = "Emoji Guess: Which country is represented here?",
            questionType = QuestionType.EMOJI,
            visualClue = "🦘 + 🐨 + 🏄‍♂️",
            answer = "Australia",
            optionA = "New Zealand",
            optionB = "Australia",
            optionC = "South Africa",
            optionD = "United Kingdom",
            explanation = "Kangaroos and koalas are native to Australia.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "countries",
            questionText = "What is the capital city of Canada?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🍁",
            answer = "Ottawa",
            optionA = "Toronto",
            optionB = "Vancouver",
            optionC = "Montreal",
            optionD = "Ottawa",
            explanation = "Ottawa was chosen by Queen Victoria as the capital of Canada in 1857.",
            difficulty = Difficulty.MEDIUM
        ),

        // CARS
        Question(
            categoryId = "cars",
            questionText = "Emoji Guess: Guess the electric automaker!",
            questionType = QuestionType.EMOJI,
            visualClue = "⚡ + 🚗 + 🚀",
            answer = "Tesla",
            optionA = "Rivian",
            optionB = "Tesla",
            optionC = "Lucid",
            optionD = "Porsche",
            explanation = "Tesla is famous for electric cars and led by SpaceX founder Elon Musk.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "cars",
            questionText = "Which luxury brand uses the famous Prancing Horse badge?",
            questionType = QuestionType.LOGO,
            visualClue = "🐎",
            answer = "Ferrari",
            optionA = "Lamborghini",
            optionB = "Porsche",
            optionC = "Ferrari",
            optionD = "Ford Mustang",
            explanation = "Ferrari's Cavallino Rampante (Prancing Horse) is legendary in Maranello.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "cars",
            questionText = "Which automaker makes the iconic 911 sports car?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🏎️",
            answer = "Porsche",
            optionA = "BMW",
            optionB = "Audi",
            optionC = "Porsche",
            optionD = "Mercedes-Benz",
            explanation = "The Porsche 911 rear-engine sports car has been in production since 1964.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "cars",
            questionText = "Which Japanese brand is known for the slogan 'The Power of Dreams'?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "✨",
            answer = "Honda",
            optionA = "Toyota",
            optionB = "Nissan",
            optionC = "Honda",
            optionD = "Subaru",
            explanation = "Honda adopted 'The Power of Dreams' as their global brand slogan.",
            difficulty = Difficulty.MEDIUM
        ),

        // PHONES
        Question(
            categoryId = "phones",
            questionText = "Emoji Guess: Which smartphone brand is this?",
            questionType = QuestionType.EMOJI,
            visualClue = "🍎 + 📱",
            answer = "Apple iPhone",
            optionA = "Samsung Galaxy",
            optionB = "Google Pixel",
            optionC = "Apple iPhone",
            optionD = "OnePlus",
            explanation = "Apple launched the first iPhone in 2007.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "phones",
            questionText = "Which phone line introduced the S Pen stylus and Ultra cameras?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🖊️",
            answer = "Samsung Galaxy",
            optionA = "Xiaomi",
            optionB = "Samsung Galaxy",
            optionC = "Sony Xperia",
            optionD = "Motorola",
            explanation = "Samsung Galaxy Note and S-Ultra series are iconic for the S-Pen stylus.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "phones",
            questionText = "Which company develops the Android operating system?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🤖",
            answer = "Google",
            optionA = "Apple",
            optionB = "Microsoft",
            optionC = "Google",
            optionD = "Meta",
            explanation = "Google acquired Android Inc. in 2005 and released the first commercial version in 2008.",
            difficulty = Difficulty.EASY
        ),

        // TECHNOLOGY
        Question(
            categoryId = "tech",
            questionText = "Emoji Guess: Guess this global tech company!",
            questionType = QuestionType.EMOJI,
            visualClue = "🔍 + 🌐 + 🤖",
            answer = "Google",
            optionA = "Yahoo",
            optionB = "Google",
            optionC = "Microsoft",
            optionD = "DuckDuckGo",
            explanation = "Google started as a search engine founded by Larry Page and Sergey Brin.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "tech",
            questionText = "What does the abbreviation 'AI' stand for?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🧠",
            answer = "Artificial Intelligence",
            optionA = "Automated Interface",
            optionB = "Artificial Intelligence",
            optionC = "Advanced Integration",
            optionD = "Applied Informatics",
            explanation = "AI stands for Artificial Intelligence.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "tech",
            questionText = "Which company created ChatGPT?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "💬",
            answer = "OpenAI",
            optionA = "Meta",
            optionB = "Amazon",
            optionC = "OpenAI",
            optionD = "Apple",
            explanation = "OpenAI launched ChatGPT in November 2022.",
            difficulty = Difficulty.EASY
        ),

        // FOOD
        Question(
            categoryId = "food",
            questionText = "Emoji Guess: Guess this popular Italian dish!",
            questionType = QuestionType.EMOJI,
            visualClue = "🧀 + 🍅 + 🫓 + 🍕",
            answer = "Pizza Margherita",
            optionA = "Lasagna",
            optionB = "Pizza Margherita",
            optionC = "Tacos",
            optionD = "Calzone",
            explanation = "Margherita pizza features tomato, mozzarella cheese, and fresh basil.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "food",
            questionText = "Which country invented Sushi?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🍣",
            answer = "Japan",
            optionA = "China",
            optionB = "Japan",
            optionC = "Vietnam",
            optionD = "Korea",
            explanation = "Modern sushi developed in Edo (now Tokyo), Japan.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "food",
            questionText = "What is the primary ingredient in traditional guacamole?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🥑",
            answer = "Avocado",
            optionA = "Tomato",
            optionB = "Cucumber",
            optionC = "Avocado",
            optionD = "Lime",
            explanation = "Guacamole is an avocado-based dip originating from Mexico.",
            difficulty = Difficulty.EASY
        ),

        // MOVIES
        Question(
            categoryId = "movies",
            questionText = "Emoji Guess: Guess the animated Disney movie!",
            questionType = QuestionType.EMOJI,
            visualClue = "🦁 + 👑 + 🌅",
            answer = "The Lion King",
            optionA = "Madagascar",
            optionB = "The Jungle Book",
            optionC = "The Lion King",
            optionD = "Tarzan",
            explanation = "The Lion King tells the story of Simba, prince of the Pride Lands.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "movies",
            questionText = "Emoji Guess: Which superhero movie is this?",
            questionType = QuestionType.EMOJI,
            visualClue = "🕷️ + 🦸‍♂️ + 🏙️",
            answer = "Spider-Man",
            optionA = "Batman",
            optionB = "Spider-Man",
            optionC = "Ant-Man",
            optionD = "Iron Man",
            explanation = "Peter Parker was bitten by a radioactive spider to become Spider-Man.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "movies",
            questionText = "Which movie features the quote 'May the Force be with you'?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "⚔️",
            answer = "Star Wars",
            optionA = "Star Trek",
            optionB = "Star Wars",
            optionC = "Lord of the Rings",
            optionD = "Matrix",
            explanation = "'May the Force be with you' is the hallmark blessing of the Jedi Order.",
            difficulty = Difficulty.EASY
        ),

        // FOOTBALL
        Question(
            categoryId = "football",
            questionText = "Which national team has won the most FIFA World Cup titles (5)?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🏆",
            answer = "Brazil",
            optionA = "Germany",
            optionB = "Italy",
            optionC = "Argentina",
            optionD = "Brazil",
            explanation = "Brazil won the World Cup in 1958, 1962, 1970, 1994, and 2002.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "football",
            questionText = "Emoji Guess: Which football superstar is this?",
            questionType = QuestionType.EMOJI,
            visualClue = "🐐 + 🇦🇷 + 🔟 + 🏆",
            answer = "Lionel Messi",
            optionA = "Cristiano Ronaldo",
            optionB = "Lionel Messi",
            optionC = "Diego Maradona",
            optionD = "Neymar Jr",
            explanation = "Lionel Messi captained Argentina to the 2022 FIFA World Cup title.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "football",
            questionText = "Which football club is known as 'The Red Devils'?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "😈",
            answer = "Manchester United",
            optionA = "Liverpool",
            optionB = "Arsenal",
            optionC = "Manchester United",
            optionD = "AC Milan",
            explanation = "Manchester United was given the nickname 'The Red Devils' by Sir Matt Busby.",
            difficulty = Difficulty.EASY
        ),

        // BRANDS & LOGOS
        Question(
            categoryId = "brands",
            questionText = "Emoji Guess: Guess this athletic brand by its motto 'Just Do It'!",
            questionType = QuestionType.EMOJI,
            visualClue = "👟 + ✔️",
            answer = "Nike",
            optionA = "Adidas",
            optionB = "Puma",
            optionC = "Nike",
            optionD = "Under Armour",
            explanation = "Nike's famous swoosh and 'Just Do It' slogan were introduced in 1988.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "brands",
            questionText = "Which coffee brand features a green two-tailed mermaid (Siren) logo?",
            questionType = QuestionType.LOGO,
            visualClue = "🧜‍♀️",
            answer = "Starbucks",
            optionA = "Dunkin'",
            optionB = "Costa Coffee",
            optionC = "Starbucks",
            optionD = "Tim Hortons",
            explanation = "Starbucks was founded in Seattle in 1971 and features a twin-tailed siren.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "brands",
            questionText = "What color is the Twitter / X bird originally known for?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🐦",
            answer = "Sky Blue",
            optionA = "Green",
            optionB = "Sky Blue",
            optionC = "Purple",
            optionD = "Yellow",
            explanation = "Twitter's mascot 'Larry the Bird' was light blue.",
            difficulty = Difficulty.EASY
        ),

        // ANIMALS
        Question(
            categoryId = "animals",
            questionText = "Emoji Guess: Guess this animal!",
            questionType = QuestionType.EMOJI,
            visualClue = "🎋 + 🐼 + 🇨🇳",
            answer = "Giant Panda",
            optionA = "Koala",
            optionB = "Giant Panda",
            optionC = "Polar Bear",
            optionD = "Sloth",
            explanation = "Giant Pandas eat almost exclusively bamboo and are native to south-central China.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "animals",
            questionText = "What is the largest living mammal on Earth?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🐋",
            answer = "Blue Whale",
            optionA = "African Elephant",
            optionB = "Blue Whale",
            optionC = "Giraffe",
            optionD = "Colossal Squid",
            explanation = "The blue whale can reach lengths of up to 30 meters and weigh up to 190 tonnes.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "animals",
            questionText = "Which bird is the fastest runner in the world?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "💨",
            answer = "Ostrich",
            optionA = "Emu",
            optionB = "Ostrich",
            optionC = "Penguin",
            optionD = "Roadrunner",
            explanation = "Ostriches can sprint at speeds up to 70 km/h (43 mph).",
            difficulty = Difficulty.EASY
        ),

        // MUSIC
        Question(
            categoryId = "music",
            questionText = "Emoji Guess: Guess the King of Pop!",
            questionType = QuestionType.EMOJI,
            visualClue = "🕺 + 🧤 + 🌕🚶‍♂️",
            answer = "Michael Jackson",
            optionA = "Prince",
            optionB = "Elvis Presley",
            optionC = "Michael Jackson",
            optionD = "Freddie Mercury",
            explanation = "Michael Jackson revolutionized pop music and introduced the Moonwalk dance in 1983.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "music",
            questionText = "How many strings does a standard acoustic guitar usually have?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🎸",
            answer = "6",
            optionA = "4",
            optionB = "5",
            optionC = "6",
            optionD = "8",
            explanation = "A standard guitar has 6 strings tuned E-A-D-G-B-E.",
            difficulty = Difficulty.EASY
        ),

        // FASHION
        Question(
            categoryId = "fashion",
            questionText = "Which French luxury house uses the interlocking 'CC' logo?",
            questionType = QuestionType.LOGO,
            visualClue = "👜",
            answer = "Chanel",
            optionA = "Louis Vuitton",
            optionB = "Gucci",
            optionC = "Chanel",
            optionD = "Dior",
            explanation = "Coco Chanel founded the iconic fashion house in Paris in 1910.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "fashion",
            questionText = "Emoji Guess: Guess this popular casual pants style!",
            questionType = QuestionType.EMOJI,
            visualClue = "👖 + 💙 + 🧵",
            answer = "Blue Jeans",
            optionA = "Chinos",
            optionB = "Blue Jeans",
            optionC = "Cargo Pants",
            optionD = "Sweatpants",
            explanation = "Levi Strauss and Jacob Davis patented copper-riveted blue denim jeans in 1873.",
            difficulty = Difficulty.EASY
        ),

        // FAMOUS PLACES
        Question(
            categoryId = "places",
            questionText = "Emoji Guess: Guess this iconic world wonder!",
            questionType = QuestionType.EMOJI,
            visualClue = "🇫🇷 + 🗼 + 🥐",
            answer = "Eiffel Tower",
            optionA = "Colosseum",
            optionB = "Big Ben",
            optionC = "Eiffel Tower",
            optionD = "Statue of Liberty",
            explanation = "The Eiffel Tower was built for the 1889 World's Fair in Paris.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "places",
            questionText = "In which country is the Taj Mahal located?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🕌",
            answer = "India",
            optionA = "Pakistan",
            optionB = "India",
            optionC = "Turkey",
            optionD = "Egypt",
            explanation = "The Taj Mahal is an ivory-white marble mausoleum in Agra, India.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "places",
            questionText = "In which city can you visit the ancient Colosseum?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🏛️",
            answer = "Rome",
            optionA = "Athens",
            optionB = "Rome",
            optionC = "Venice",
            optionD = "Madrid",
            explanation = "The Colosseum was completed in 80 AD in Rome under emperor Titus.",
            difficulty = Difficulty.EASY
        ),

        // ETHIOPIA
        Question(
            categoryId = "ethiopia",
            questionText = "Emoji Guess: What world-famous drink traces its botanical origin to Ethiopia?",
            questionType = QuestionType.EMOJI,
            visualClue = "☕ + ⛰️ + 🇪🇹",
            answer = "Coffee",
            optionA = "Tea",
            optionB = "Cocoa",
            optionC = "Coffee",
            optionD = "Chai",
            explanation = "According to legend, the coffee plant was discovered in the Kaffa region of Ethiopia by the goat herder Kaldi.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "ethiopia",
            questionText = "Which historic town in Ethiopia is famous for its 11 monolithic rock-hewn churches?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "⛪",
            answer = "Lalibela",
            optionA = "Gondar",
            optionB = "Axum",
            optionC = "Lalibela",
            optionD = "Harar",
            explanation = "The rock-hewn churches of Lalibela, carved directly into volcanic rock in the 12th century, are a UNESCO World Heritage site.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "ethiopia",
            questionText = "What is the name of the traditional sourdough flatbread made from Teff grain?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🫓",
            answer = "Injera",
            optionA = "Pita",
            optionB = "Naan",
            optionC = "Injera",
            optionD = "Roti",
            explanation = "Injera is a spongy flatbread central to Ethiopian dining.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "ethiopia",
            questionText = "What is the capital city of Ethiopia, home to the African Union headquarters?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🏙️",
            answer = "Addis Ababa",
            optionA = "Nairobi",
            optionB = "Addis Ababa",
            optionC = "Asmara",
            optionD = "Djibouti",
            explanation = "Addis Ababa ('New Flower' in Amharic) serves as diplomatic capital of Africa.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "ethiopia",
            questionText = "Which legendary Ethiopian runner won the 1960 Olympic Marathon barefoot?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🏃‍♂️",
            answer = "Abebe Bikila",
            optionA = "Haile Gebrselassie",
            optionB = "Kenenisa Bekele",
            optionC = "Abebe Bikila",
            optionD = "Derartu Tulu",
            explanation = "Abebe Bikila won gold in Rome 1960 running barefoot, the first sub-Saharan African Olympic gold medalist.",
            difficulty = Difficulty.MEDIUM
        ),
        Question(
            categoryId = "ethiopia",
            questionText = "What is the famous 3.2-million-year-old hominid fossil discovered in Ethiopia named?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "🦴",
            answer = "Lucy (Dinkinesh)",
            optionA = "Lucy (Dinkinesh)",
            optionB = "Ardi",
            optionC = "Taung Child",
            optionD = "Turkana Boy",
            explanation = "Australopithecus afarensis fossil 'Lucy' (Dinkinesh, meaning 'you are marvelous') was discovered in 1974 in the Afar Depression.",
            difficulty = Difficulty.MEDIUM
        ),

        // GENERAL KNOWLEDGE
        Question(
            categoryId = "general",
            questionText = "How many days are in a leap year?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "📅",
            answer = "366",
            optionA = "364",
            optionB = "365",
            optionC = "366",
            optionD = "368",
            explanation = "A leap year has 366 days, adding February 29th.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "general",
            questionText = "Emoji Guess: Guess this planet in our solar system!",
            questionType = QuestionType.EMOJI,
            visualClue = "🔴 + 🪐 + 🛸",
            answer = "Mars",
            optionA = "Venus",
            optionB = "Jupiter",
            optionC = "Mars",
            optionD = "Saturn",
            explanation = "Mars is known as the Red Planet due to iron oxide on its surface.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "general",
            questionText = "What is the chemical formula for water?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "💧",
            answer = "H2O",
            optionA = "CO2",
            optionB = "H2O",
            optionC = "NaCl",
            optionD = "O2",
            explanation = "Water is composed of two hydrogen atoms bonded to one oxygen atom.",
            difficulty = Difficulty.EASY
        ),

        // FUN & RANDOM
        Question(
            categoryId = "fun",
            questionText = "Emoji Riddle: Guess the phrase!",
            questionType = QuestionType.EMOJI,
            visualClue = "🌧️ + 🐱 + 🐶",
            answer = "Raining cats and dogs",
            optionA = "Animal shelter",
            optionB = "Raining cats and dogs",
            optionC = "Pet storm",
            optionD = "Thunder paws",
            explanation = "'Raining cats and dogs' is an English idiom meaning raining heavily.",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "fun",
            questionText = "What has hands but cannot clap?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            visualClue = "⏰",
            answer = "A Clock",
            optionA = "A Statue",
            optionB = "A Clock",
            optionC = "A Glove",
            optionD = "A Tree",
            explanation = "A clock has hour and minute hands!",
            difficulty = Difficulty.EASY
        ),
        Question(
            categoryId = "fun",
            questionText = "Emoji Guess: Guess the fairy tale!",
            questionType = QuestionType.EMOJI,
            visualClue = "👸 + 👠 + 🎃 + 🕛",
            answer = "Cinderella",
            optionA = "Snow White",
            optionB = "Sleeping Beauty",
            optionC = "Cinderella",
            optionD = "Rapunzel",
            explanation = "Cinderella loses her glass slipper when the clock strikes midnight.",
            difficulty = Difficulty.EASY
        )
    )

    fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}
