public class Enums {
    enum Role{
        STRATEGIST("Focuses on tactics and planning. Keeps the bigger picture in mind during gameplay."),
        ATTACKER("Frontline player. Good reflexes, offensive tactics, quick execution."),
        DEFENDER("Protects and supports team stability. Good under pressure and team-focused."),
        SUPPORTER("Jack-of-all-trades. Adapts roles, ensures smooth coordination."),
        COORDINATOR("Communication lead. Keeps the team informed and organized in real time.");

        private String description;

        Role(String description){
            this.description = description;
        }

        public String getDescription(){
            return description;
        }
    }

    enum Game{
        VALORANT,
        CSGO,
        FIFA,
        DOTA2,
        CHESS,
        BASKETBALL;
    }
}
