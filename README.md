# TeamMate: Automated Team Formation System [cite: 23]

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Principles-blue?style=for-the-badge)

"TeamMate" is a Java-based console application designed to automate team formation for a University Gaming Club[cite: 23, 33]. [cite_start]The system models a real-world software solution by evaluating participant survey data and grouping them into highly balanced, competitive teams[cite: 24, 34, 35].

## 🚀 Technical Highlights

* **Object-Oriented Architecture:** Designed using core OOP principles including inheritance, encapsulation, abstraction, and polymorphism[cite: 25].
* **Custom Matching Algorithm:** Implements a dynamic algorithm to allocate teams based on game type, skill level, preferred role, and personality traits to ensure fair competition[cite: 35].
* **Concurrency:** Integrates multi-threading and concurrency mechanisms within the organizer module for optimized data processing[cite: 24, 333].
* **Data Persistence:** Utilizes robust file handling to load participant data and export formed teams using CSV files without relying on third-party services[cite: 26, 36, 69].
* **Input Validation:** Features an `InputValidator` class that strictly validates all user inputs to prevent runtime crashes[cite: 51, 332].

## ⚙️ How It Works

1. **Data Collection:** Participants complete a built-in survey to record their gaming preferences, skill levels, and answer questions to classify their personality type[cite: 34, 95].
2. **System Processing:** The application parses uploaded CSV data and instantiates participant objects[cite: 98].
3. **Team Generation:** Organizers define a specific team size, and the matching algorithm automatically generates teams with diverse roles and nearly equal average skill scores[cite: 46, 47, 101].
4. **Supported Games:** The system maps players for popular competitive titles including VALORANT, CSGO, DOTA2, and FIFA[cite: 195, 197, 198, 199, 200].

## 💻 Usage

The application features a console-based user interface that separates functionalities into distinct roles:

* **Organizers:** Can upload participant CSV files, define team sizes, initiate the team formation algorithm, view the generated teams, and export the final results[cite: 69, 101, 104, 110].
* **Participants:** Can access the system to complete their personality and skill surveys, which feed directly into the matching algorithm[cite: 95].
