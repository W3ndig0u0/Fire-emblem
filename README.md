# Fire Emblem: Strategy Engine

A sophisticated **Tactical RPG Engine** inspired by the classic **Fire Emblem** series, built with **Java** and **Spring Boot**. This project transitions traditional game mechanics into a robust backend architecture, managing complex combat simulations, unit statistics, and turn-based strategy logic.

---

## ⚔️ Project Overview

This is not just a normal CRUD app; it's a **Game Logic Engine**. The project replicates the core systems of a strategic RPG, focusing on the intricate mathematics behind combat, weapon durability, and character growth. It demonstrates high-level **Object-Oriented Programming (OOP)** and state management within a Spring framework.
And showcasing it with React on the frontend.

---

## 🛠 Tech Stack & Architecture

*   **Core:** Java 17, **Spring Boot 3**
*   **Game Logic:** Advanced OOP (Inheritance, Polymorphism, Encapsulation)
*   **Persistence:** **MySQL** (handling unit stats, inventory, and save states)
*   **ORM:** Spring Data JPA / Hibernate
*   **Build Tool:** Maven
*   **Testing:** JUnit 5 (Crucial for verifying combat formulas and RNG outcomes)

---

## ✨ Key Features & Mechanics

### 🤺 Combat Simulation Engine
*   **RNG-Based Hits:** Implements accuracy, crit rates, and avoidance formulas based on unit speed and luck.
*   **Weapon Triangle System:** Logic-driven advantages/disadvantages (Sword > Axe > Lance) implemented via backend services.
*   **Damage Calculation:** Handles complex interactions between Strength/Magic vs. Defense/Resistance.

### 📈 Unit & Growth Management
*   **Dynamic Leveling:** A custom "Level Up" system that calculates stat gains based on hidden percentage growth rates.
*   **Class System:** Modular design allowing for different unit types (Cavalier, Mage, Archer) with unique movement and stat caps.
*   **Permadeath Logic:** Backend flags to handle unit availability and graveyard states.

### 🎒 Inventory & Durability
*   **Weapon Degradation:** Tracks usage and handles item breakage when durability hits zero.
*   **Item Management:** One-to-Many relationships between Units and their carried Items/Weapons.

---

## 🚀 Installation & Setup

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com
    ```
2.  **Database Configuration:**
    Configure your MySQL settings in `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/fire_emblem_db
    ```
3.  **Run the Engine:**
    ```bash
    mvn spring-boot:run
    ```

---

## 🚧 Roadmap
- [ ] Implement a Fog of War logic in the backend.
- [ ] Add Support Rank bonuses between units.
- [ ] Add Tiles where character can't go on, like buildings
- [ ] Develop a React-based frontend to visualize the grid-based combat.
- [ ] Implement a big engine for easer implementation of levels
