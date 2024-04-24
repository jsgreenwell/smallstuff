import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Monster monster = new Monster();
        monster.loadTitle();
        Scanner scan = new Scanner(System.in);
        scan.nextLine();
        monster.monsterImage();
        System.out.println(monster);
        monster.printToFile("data/monster.txt");
    }
}