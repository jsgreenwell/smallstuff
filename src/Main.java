public class Main {
    public static void main(String[] args) {
        Monster monster = new Monster();
        monster.loadTitle();
        monster.monsterImage();
        System.out.println(monster);
        monster.printToFile("data/monster.txt");
    }
}