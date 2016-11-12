import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by 36526 on 2016/11/1.
 */

/**
 * Class <code>AnimalChess</code> is the root of the class hierarchy.
 * Every class has <code>AnimalChess</code> as a superclass. All objects,
 * including arrays, implement the methods of this class.
 */
public class AnimalChess {
    //先建立好两个公共数组，下面都要用的~
    static String[][] animal = new String[7][9];
    static String[][] map = new String[7][9];
    static String[][][] animalHistory = new String[1000000][][];//建立一个用来悔棋的三维数组
    //悔棋原理是用第三个维度去记录棋盘的状态

    /**
     * Main method is to move the animals and process
     * the specified operations from the users
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        int player = 0;
        int lastStep = 0;//这两步得益于Lab7！！绝妙！！
        animal = getAnimal();
        map = getMap();
        animalHistory[player] = AnimalChess.copyArray(animal);//记录初始状态的棋盘
        System.out.println("WOW斗兽棋！！！It's your Show Time!!");
        printLandscape();

        Scanner input = new Scanner(System.in);
        stop:
        while (true) {
            if (player % 2 == 0) {
                int m1 = 0;
                int m2 = 0;
                if (checkHaveAnimals()) {
                    System.out.println("RIGHT!!!Win!!!!!!!");
                    break stop;
                }//检查左方是否还有动物，若没有，则右赢；否则，继续游戏
                System.out.print("左方玩家行动： ");
                String s = input.nextLine();
                System.out.print(s + "\n");

                if (s.equals("help")) {
                    printRules();//打印规则

                } else if (s.equals("undo")) {
                    if (player >= 1) {
                        player -= 1;
                        animal = AnimalChess.copyArray(animalHistory[player]);//实现悔棋
                        printLandscape();
                    } else {
                        System.out.println("已经退回到开局啦！!");
                    }

                } else if (s.equals("redo")) {
                    if (player + 1 <= lastStep) {
                        player += 1;
                        animal = AnimalChess.copyArray(animalHistory[player]);//实现撤销悔棋
                        printLandscape();
                    } else {
                        System.out.println("后面还没下啊！!");
                    }

                } else if (s.equals("exit")) {
                    break stop;

                } else if (s.equals("restart")) {
                    //清空棋盘状态，一切清零
                    player = 0;
                    lastStep = 0;
                    animal = AnimalChess.copyArray(animalHistory[0]);
                    System.out.println("新一局诶！！Come On!!");
                    printLandscape();

                } else if (isValidInput(s)) {
                    String s1 = s.substring(0, 1);
                    String s2 = s.substring(1, 2);
                    switch (s1) {
                        case "1":
                            m1 = assureRow("1鼠 ");
                            m2 = assureColumn("1鼠 ");
                            break;
                        case "2":
                            m1 = assureRow("2猫 ");
                            m2 = assureColumn("2猫 ");
                            break;
                        case "3":
                            m1 = assureRow("3狼 ");
                            m2 = assureColumn("3狼 ");
                            break;
                        case "4":
                            m1 = assureRow("4狗 ");
                            m2 = assureColumn("4狗 ");
                            break;
                        case "5":
                            m1 = assureRow("5豹 ");
                            m2 = assureColumn("5豹 ");
                            break;
                        case "6":
                            m1 = assureRow("6虎 ");
                            m2 = assureColumn("6虎 ");
                            break;
                        case "7":
                            m1 = assureRow("7狮 ");
                            m2 = assureColumn("7狮 ");
                            break;
                        case "8":
                            m1 = assureRow("8象 ");
                            m2 = assureColumn("8象 ");
                            break;
                    }
                    //找到输入的数字所对应的左方的动物的行与列，从而进行下一步操作
                    switch (s2) {
                        //先判断是否这个动物还活着，在判断接下来会不会走出边界，再判断陷阱与兽穴，再分动物讨论具体走法
                        case "d":
                            if (m1 == -1) {
                                System.out.println("您选的动物已阵亡orz");
                                continue stop;
                            } else if (m2 == 8) {
                                System.out.println("不能走出边界");
                                continue stop;
                            } else if (!animal[m1][m2 + 1].substring(2, 3).equals(" ") && map[m1][m2 + 1].equals("0陷 ")) {
                                animal[m1][m2 + 1] = animal[m1][m2];
                                animal[m1][m2] = " 　 ";
                                printLandscape();
                            } else if (map[m1][m2 + 1].equals(" 陷0")) {
                                if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 + 1].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (map[m1][m2 + 1].equals("0穴 ")) {
                                System.out.println("不能回家！！就是刚！");
                                continue stop;
                            } else if (map[m1][m2 + 1].equals(" 穴0")) {
                                System.out.println("LEFT!!!!Win!!!!!!!");
                                break stop;
                            } else if (animal[m1][m2].equals("1鼠 ")) {
                                if (!animal[m1][m2 + 1].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 + 1].substring(2, 3)) < 0 && !animal[m1][m2 + 1].substring(2, 3).equals("8")) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2].equals(" 水 ") && animal[m1][m2 + 1].equals(" 象8")) {
                                    System.out.println("水里不能强吃哦~");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals("8象 ")) {
                                if (!animal[m1][m2 + 1].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2 + 1].substring(2, 3).equals("1")) {
                                    System.out.println("快跑！！小老鼠你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2 + 1].equals(" 水 ")) {
                                    System.out.println("象不能下水");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals("6虎 ") || animal[m1][m2].equals("7狮 ")) {
                                if (map[m1][m2 + 1].equals(" 水 ")) {
                                    if (animal[m1][m2 + 1].equals(" 鼠1") || animal[m1][m2 + 2].equals(" 鼠1") || animal[m1][m2 + 3].equals(" 鼠1")) {
                                        System.out.println("你得等等咯！跳不过去哈~");
                                        continue stop;
                                    } else {
                                        if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 + 4].substring(2, 3)) < 0) {
                                            System.out.println("跳过去就死啊啊啊！不行~");
                                            continue stop;
                                        } else if (!animal[m1][m2 + 4].substring(0, 1).equals(" ")) {
                                            System.out.println("友方！！请注意！！");
                                            continue stop;
                                        } else {
                                            animal[m1][m2 + 4] = animal[m1][m2];
                                            animal[m1][m2] = " 　 ";
                                            printLandscape();
                                        }
                                    }
                                } else if (!animal[m1][m2 + 1].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 + 1].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else {
                                if (!animal[m1][m2 + 1].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (map[m1][m2 + 1].equals(" 水 ")) {
                                    System.out.println(animal[m1][m2].substring(1, 2) + "不能下水");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 + 1].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            }
                            break;

                        case "w":
                            if (m1 == -1) {
                                System.out.println("您选的动物已阵亡orz");
                                continue stop;
                            } else if (m1 == 0) {
                                System.out.println("不能走出边界");
                                continue stop;
                            } else if (!animal[m1 - 1][m2].substring(2, 3).equals(" ") && map[m1 - 1][m2].equals("0陷 ")) {
                                animal[m1 - 1][m2] = animal[m1][m2];
                                animal[m1][m2] = " 　 ";
                                printLandscape();
                            } else if (map[m1 - 1][m2].equals(" 陷0")) {
                                if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 - 1][m2].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }

                            } else if (map[m1 - 1][m2].equals("0穴 ")) {
                                System.out.println("不能回家！！就是刚！");
                                continue stop;
                            } else if (map[m1 - 1][m2].equals(" 穴0")) {
                                System.out.println("LEFT!!!Win!!!!!!!");
                                break stop;
                            } else if (animal[m1][m2].equals("1鼠 ")) {
                                if (!animal[m1 - 1][m2].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 - 1][m2].substring(2, 3)) < 0 && !animal[m1 - 1][m2].substring(2, 3).equals("8")) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2].equals(" 水 ") && animal[m1 - 1][m2].equals(" 象8")) {
                                    System.out.println("水里不能强吃哦~");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals("8象 ")) {
                                if (!animal[m1 - 1][m2].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1 - 1][m2].substring(2, 3).equals("1")) {
                                    System.out.println("快跑！！小老鼠你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1 - 1][m2].equals(" 水 ")) {
                                    System.out.println("象不能下水");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals("6虎 ") || animal[m1][m2].equals("7狮 ")) {
                                if (map[m1 - 1][m2].equals(" 水 ")) {
                                    if (animal[m1 - 1][m2].equals(" 鼠1") || animal[m1 - 2][m2].equals(" 鼠1")) {
                                        System.out.println("你得等等咯！跳不过去哈~");
                                        continue stop;
                                    } else {
                                        if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 - 3][m2].substring(2, 3)) < 0) {
                                            System.out.println("跳过去就死啊啊啊！不行~");
                                            continue stop;
                                        } else if (!animal[m1 - 3][m2].substring(0, 1).equals(" ")) {
                                            System.out.println("友方！！请注意！！");
                                            continue stop;
                                        } else {
                                            animal[m1 - 3][m2] = animal[m1][m2];
                                            animal[m1][m2] = " 　 ";
                                            printLandscape();
                                        }
                                    }
                                } else if (!animal[m1 - 1][m2].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 - 1][m2].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else {
                                if (!animal[m1 - 1][m2].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (map[m1 - 1][m2].equals(" 水 ")) {
                                    System.out.println(animal[m1][m2].substring(1, 2) + "不能下水");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 - 1][m2].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            }
                            break;

                        case "a":
                            if (m1 == -1) {
                                System.out.println("您选的动物已阵亡orz");
                                continue stop;
                            } else if (m2 == 0) {
                                System.out.println("不能走出边界");
                                continue stop;
                            } else if (!animal[m1][m2 - 1].substring(2, 3).equals(" ") && map[m1][m2 - 1].equals("0陷 ")) {
                                animal[m1][m2 - 1] = animal[m1][m2];
                                animal[m1][m2] = " 　 ";
                                printLandscape();
                            } else if (map[m1][m2 - 1].equals(" 陷0")) {
                                if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 - 1].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (map[m1][m2 - 1].equals("0穴 ")) {
                                System.out.println("不能回家！！就是刚！");
                                continue stop;
                            } else if (map[m1][m2 - 1].equals(" 穴0")) {
                                System.out.println("LEFT!!!Win!!!!!!!");
                                break stop;
                            } else if (animal[m1][m2].equals("1鼠 ")) {
                                if (!animal[m1][m2 - 1].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 - 1].substring(2, 3)) < 0 && !animal[m1][m2 - 1].substring(2, 3).equals("8")) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2].equals(" 水 ") && animal[m1][m2 - 1].equals(" 象8")) {
                                    System.out.println("水里不能强吃哦~");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals("8象 ")) {
                                if (!animal[m1][m2 - 1].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2 - 1].substring(2, 3).equals("1")) {
                                    System.out.println("快跑！！小老鼠你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2 - 1].equals(" 水 ")) {
                                    System.out.println("象不能下水");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals("6虎 ") || animal[m1][m2].equals("7狮 ")) {
                                if (map[m1][m2 - 1].equals(" 水 ")) {
                                    if (animal[m1][m2 - 1].equals(" 鼠1") || animal[m1][m2 - 2].equals(" 鼠1") || animal[m1][m2 - 3].equals(" 鼠1")) {
                                        System.out.println("你得等等咯！跳不过去哈~");
                                        continue stop;
                                    } else {
                                        if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 - 4].substring(2, 3)) < 0) {
                                            System.out.println("跳过去就死啊啊啊！不行~");
                                            continue stop;
                                        } else if (!animal[m1][m2 - 4].substring(0, 1).equals(" ")) {
                                            System.out.println("友方！！请注意！！");
                                            continue stop;
                                        } else {
                                            animal[m1][m2 - 4] = animal[m1][m2];
                                            animal[m1][m2] = " 　 ";
                                            printLandscape();
                                        }
                                    }
                                } else if (!animal[m1][m2 - 1].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 - 1].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else {
                                if (!animal[m1][m2 - 1].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (map[m1][m2 - 1].equals(" 水 ")) {
                                    System.out.println(animal[m1][m2].substring(1, 2) + "不能下水");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1][m2 - 1].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            }
                            break;

                        case "s":
                            if (m1 == -1) {
                                System.out.println("您选的动物已阵亡orz");
                                continue stop;
                            } else if (m1 == 6) {
                                System.out.println("不能走出边界");
                                continue stop;
                            } else if (!animal[m1 + 1][m2].substring(2, 3).equals(" ") && map[m1 + 1][m2].equals("0陷 ")) {
                                animal[m1 + 1][m2] = animal[m1][m2];
                                animal[m1][m2] = " 　 ";
                                printLandscape();
                            } else if (map[m1 + 1][m2].equals(" 陷0")) {
                                if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 + 1][m2].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (map[m1 + 1][m2].equals("0穴 ")) {
                                System.out.println("不能回家！！就是刚！");
                                continue stop;
                            } else if (map[m1 + 1][m2].equals(" 穴0")) {
                                System.out.println("LEFT!!!Win!!!!!!!");
                                break stop;
                            } else if (animal[m1][m2].equals("1鼠 ")) {
                                if (!animal[m1 + 1][m2].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 + 1][m2].substring(2, 3)) < 0 && !animal[m1 + 1][m2].substring(2, 3).equals("8")) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2].equals(" 水 ") && animal[m1 + 1][m2].equals(" 象8")) {
                                    System.out.println("水里不能强吃哦~");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals("8象 ")) {
                                if (!animal[m1 + 1][m2].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1 + 1][m2].substring(2, 3).equals("1")) {
                                    System.out.println("快跑！！小老鼠你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1 + 1][m2].equals(" 水 ")) {
                                    System.out.println("象不能下水");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals("6虎 ") || animal[m1][m2].equals("7狮 ")) {
                                if (map[m1 + 1][m2].equals(" 水 ")) {
                                    if (animal[m1 + 1][m2].equals(" 鼠1") || animal[m1 + 2][m2].equals(" 鼠1")) {
                                        System.out.println("你得等等咯！跳不过去哈~");
                                        continue stop;
                                    } else {
                                        if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 + 3][m2].substring(2, 3)) < 0) {
                                            System.out.println("跳过去就死啊啊啊！不行~");
                                            continue stop;
                                        } else if (!animal[m1 + 3][m2].substring(0, 1).equals(" ")) {
                                            System.out.println("友方！！请注意！！");
                                            continue stop;
                                        } else {
                                            animal[m1 + 3][m2] = animal[m1][m2];
                                            animal[m1][m2] = " 　 ";
                                            printLandscape();
                                        }
                                    }
                                } else if (!animal[m1 + 1][m2].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 + 1][m2].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else {
                                if (!animal[m1 + 1][m2].substring(0, 1).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (map[m1 + 1][m2].equals(" 水 ")) {
                                    System.out.println(animal[m1][m2].substring(1, 2) + "不能下水");
                                    continue stop;
                                } else if (animal[m1][m2].substring(0, 1).compareTo(animal[m1 + 1][m2].substring(2, 3)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            }
                            break;
                    }
                    player += 1;
                    lastStep = player;
                    animalHistory[player] = AnimalChess.copyArray(animal);//记录此时棋盘状态
                } else {
                    System.out.println("\n不能识别指令: " + "\"" + s + "\"" + ",请输入help看一下怎么下哈~");
                    continue stop;
                }

            } else {
                int m1 = 0;
                int m2 = 0;
                if (checkHaveAnimals()) {
                    System.out.println("LEFT!!!!Win!!!!!!!");
                    break stop;
                }//检查右方是否还有动物，若没有，则左赢；否则，继续游戏
                System.out.print("右方玩家行动： ");
                String s = input.nextLine();
                System.out.print(s + "\n");

                if (s.equals("help")) {
                    printRules();//打印规则

                } else if (s.equals("undo")) {
                    if (player >= 1) {
                        player -= 1;
                        animal = AnimalChess.copyArray(animalHistory[player]);//调用上一步棋盘状态
                        printLandscape();
                    } else {
                        System.out.println("Invalid undo!");
                    }

                } else if (s.equals("redo")) {
                    if (player + 1 <= lastStep) {
                        player += 1;
                        animal = AnimalChess.copyArray(animalHistory[player]);//调用可用的下一步棋盘状态
                        printLandscape();
                    } else {
                        System.out.println("Invalid Redo!");
                    }

                } else if (s.equals("exit")) {
                    break stop;

                } else if (s.equals("restart")) {
                    player = 0;
                    lastStep = 0;
                    animal = AnimalChess.copyArray(animalHistory[0]);
                    System.out.println("新一局诶！！Come On!!");
                    printLandscape();//清空棋盘状态

                } else if (isValidInput(s)) {
                    String s1 = s.substring(0, 1);
                    String s2 = s.substring(1, 2);
                    switch (s1) {
                        case "1":
                            m1 = assureRow(" 鼠1");
                            m2 = assureColumn(" 鼠1");
                            break;
                        case "2":
                            m1 = assureRow(" 猫2");
                            m2 = assureColumn(" 猫2");
                            break;
                        case "3":
                            m1 = assureRow(" 狼3");
                            m2 = assureColumn(" 狼3");
                            break;
                        case "4":
                            m1 = assureRow(" 狗4");
                            m2 = assureColumn(" 狗4");
                            break;
                        case "5":
                            m1 = assureRow(" 豹5");
                            m2 = assureColumn(" 豹5");
                            break;
                        case "6":
                            m1 = assureRow(" 虎6");
                            m2 = assureColumn(" 虎6");
                            break;
                        case "7":
                            m1 = assureRow(" 狮7");
                            m2 = assureColumn(" 狮7");
                            break;
                        case "8":
                            m1 = assureRow(" 象8");
                            m2 = assureColumn(" 象8");
                            break;
                    }
                    //找到输入的数字代表的右方动物所在的行和列，从而进行移动
                    switch (s2) {
                        case "d":
                            if (m1 == -1) {
                                System.out.println("您选的动物已阵亡orz");
                                continue stop;
                            } else if (m2 == 8) {
                                System.out.println("不能走出边界");
                                continue stop;
                            } else if (!animal[m1][m2 + 1].substring(0, 1).equals(" ") && map[m1][m2 + 1].equals(" 陷0")) {
                                animal[m1][m2 + 1] = animal[m1][m2];
                                animal[m1][m2] = " 　 ";
                                printLandscape();
                            } else if (map[m1][m2 + 1].equals("0陷 ")) {
                                if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 + 1].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (map[m1][m2 + 1].equals(" 穴0")) {
                                System.out.println("不能回家！！就是刚！");
                                continue stop;
                            } else if (map[m1][m2 + 1].equals("0穴 ")) {
                                System.out.println("RIGHT!!!!Win!!!!!!!");
                                break stop;
                            } else if (animal[m1][m2].equals(" 鼠1")) {
                                if (!animal[m1][m2 + 1].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 + 1].substring(0, 1)) < 0 && !animal[m1][m2 + 1].substring(0, 1).equals("8")) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2].equals(" 水 ") && animal[m1][m2 + 1].equals("8象 ")) {
                                    System.out.println("水里不能强吃哦~");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals(" 象8")) {
                                if (!animal[m1][m2 + 1].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2 + 1].substring(0, 1).equals("1")) {
                                    System.out.println("快跑！！小老鼠你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2 + 1].equals(" 水 ")) {
                                    System.out.println("象不能下水");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals(" 虎6") || animal[m1][m2].equals(" 狮7")) {
                                if (map[m1][m2 + 1].equals(" 水 ")) {
                                    if (animal[m1][m2 + 1].equals("1鼠 ") || animal[m1][m2 + 2].equals("1鼠 ") || animal[m1][m2 + 3].equals("1鼠 ")) {
                                        System.out.println("你得等等咯！跳不过去哈~");
                                        continue stop;
                                    } else {
                                        if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 + 4].substring(0, 1)) < 0) {
                                            System.out.println("跳过去就死啊啊啊！不行~");
                                            continue stop;
                                        } else if (!animal[m1][m2 + 4].substring(2, 3).equals(" ")) {
                                            System.out.println("友方！！请注意！！");
                                            continue stop;
                                        } else {
                                            animal[m1][m2 + 4] = animal[m1][m2];
                                            animal[m1][m2] = " 　 ";
                                            printLandscape();
                                        }
                                    }
                                } else if (!animal[m1][m2 + 1].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 + 1].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else {
                                if (!animal[m1][m2 + 1].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (map[m1][m2 + 1].equals(" 水 ")) {
                                    System.out.println(animal[m1][m2].substring(1, 2) + "不能下水");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 + 1].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 + 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            }
                            break;

                        case "w":
                            if (m1 == -1) {
                                System.out.println("您选的动物已阵亡orz");
                                continue stop;
                            } else if (m1 == 0) {
                                System.out.println("不能走出边界");
                                continue stop;
                            } else if (!animal[m1 - 1][m2].substring(0, 1).equals(" ") && map[m1 - 1][m2].equals(" 陷0")) {
                                animal[m1 - 1][m2] = animal[m1][m2];
                                animal[m1][m2] = " 　 ";
                                printLandscape();
                            }else if (map[m1 - 1][m2].equals("0陷 ")){
                                if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 - 1][m2].substring(0, 1)) < 0){
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                }else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (map[m1 - 1][m2].equals(" 穴0")) {
                                System.out.println("不能回家！！就是刚！");
                                continue stop;
                            } else if (map[m1 - 1][m2].equals("0穴 ")) {
                                System.out.println("RIGHT!!!Win!!!!!!!");
                                break stop;
                            } else if (animal[m1][m2].equals(" 鼠1")) {
                                if (!animal[m1 - 1][m2].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 - 1][m2].substring(0, 1)) < 0 && !animal[m1 - 1][m2].substring(0, 1).equals("8")) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2].equals(" 水 ") && animal[m1 - 1][m2].equals("8象 ")) {
                                    System.out.println("水里不能强吃哦~");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals(" 象8")) {
                                if (!animal[m1 - 1][m2].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1 - 1][m2].substring(0, 1).equals("1")) {
                                    System.out.println("快跑！！小老鼠你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1 - 1][m2].equals(" 水 ")) {
                                    System.out.println("象不能下水");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals(" 虎6") || animal[m1][m2].equals(" 狮7")) {
                                if (map[m1 - 1][m2].equals(" 水 ")) {
                                    if (animal[m1 - 1][m2].equals("1鼠 ") || animal[m1 - 2][m2].equals("1鼠 ")) {
                                        System.out.println("你得等等咯！跳不过去哈~");
                                        continue stop;
                                    } else {
                                        if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 - 3][m2].substring(0, 1)) < 0) {
                                            System.out.println("跳过去就死啊啊啊！不行~");
                                            continue stop;
                                        } else if (!animal[m1 - 3][m2].substring(2, 3).equals(" ")) {
                                            System.out.println("友方！！请注意！！");
                                            continue stop;
                                        } else {
                                            animal[m1 - 3][m2] = animal[m1][m2];
                                            animal[m1][m2] = " 　 ";
                                            printLandscape();
                                        }
                                    }
                                } else if (!animal[m1 - 1][m2].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 - 1][m2].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else {
                                if (!animal[m1 - 1][m2].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (map[m1 - 1][m2].equals(" 水 ")) {
                                    System.out.println(animal[m1][m2].substring(1, 2) + "不能下水");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 - 1][m2].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 - 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            }
                            break;

                        case "a":
                            if (m1 == -1) {
                                System.out.println("您选的动物已阵亡orz");
                                continue stop;
                            } else if (m2 == 0) {
                                System.out.println("不能走出边界");
                                continue stop;
                            } else if (!animal[m1][m2 - 1].substring(0, 1).equals(" ") && map[m1][m2 - 1].equals(" 陷0")) {
                                animal[m1][m2 - 1] = animal[m1][m2];
                                animal[m1][m2] = " 　 ";
                                printLandscape();
                            }else if (map[m1][m2 - 1].equals("0陷 ")){
                                if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 - 1].substring(0, 1)) < 0){
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                }else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (map[m1][m2 - 1].equals(" 穴0")) {
                                System.out.println("不能回家！！就是刚！");
                                continue stop;
                            } else if (map[m1][m2 - 1].equals("0穴 ")) {
                                System.out.println("RIGHT!!!!!Win!!!!!!!");
                                break stop;
                            } else if (animal[m1][m2].equals(" 鼠1")) {
                                if (!animal[m1][m2 - 1].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 - 1].substring(0, 1)) < 0 && !animal[m1][m2 - 1].substring(0, 1).equals("8")) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2].equals(" 水 ") && animal[m1][m2 - 1].equals("8象 ")) {
                                    System.out.println("水里不能强吃哦~");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals(" 象8")) {
                                if (!animal[m1][m2 - 1].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2 - 1].substring(0, 1).equals("1")) {
                                    System.out.println("快跑！！小老鼠你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2 - 1].equals(" 水 ")) {
                                    System.out.println("象不能下水");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals(" 虎6") || animal[m1][m2].equals(" 狮7")) {
                                if (map[m1][m2 - 1].equals(" 水 ")) {
                                    if (animal[m1][m2 - 1].equals("1鼠 ") || animal[m1][m2 - 2].equals("1鼠 ") || animal[m1][m2 - 3].equals("1鼠 ")) {
                                        System.out.println("你得等等咯！跳不过去哈~");
                                        continue stop;
                                    } else {
                                        if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 - 4].substring(0, 1)) < 0) {
                                            System.out.println("跳过去就死啊啊啊！不行~");
                                            continue stop;
                                        } else if (!animal[m1][m2 - 4].substring(2, 3).equals(" ")) {
                                            System.out.println("友方！！请注意！！");
                                            continue stop;
                                        } else {
                                            animal[m1][m2 - 4] = animal[m1][m2];
                                            animal[m1][m2] = " 　 ";
                                            printLandscape();
                                        }
                                    }
                                } else if (!animal[m1][m2 - 1].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 - 1].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else {
                                if (!animal[m1][m2 - 1].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (map[m1][m2 - 1].equals(" 水 ")) {
                                    System.out.println(animal[m1][m2].substring(1, 2) + "不能下水");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1][m2 - 1].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1][m2 - 1] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            }
                            break;

                        case "s":
                            if (m1 == -1) {
                                System.out.println("您选的动物已阵亡orz");
                                continue stop;
                            } else if (m1 == 6) {
                                System.out.println("不能走出边界");
                                continue stop;
                            } else if (!animal[m1 + 1][m2].substring(0, 1).equals(" ") && map[m1 + 1][m2].equals(" 陷0")) {
                                animal[m1 + 1][m2] = animal[m1][m2];
                                animal[m1][m2] = " 　 ";
                                printLandscape();
                            }else if (map[m1 + 1][m2].equals("0陷 ")){
                                if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 + 1][m2].substring(0, 1)) < 0){
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                }else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (map[m1 + 1][m2].equals(" 穴0")) {
                                System.out.println("不能回家！！就是刚！");
                                continue stop;
                            } else if (map[m1 + 1][m2].equals("0穴 ")) {
                                System.out.println("RIGHT!!!!!Win!!!!!!!");
                                break stop;
                            } else if (animal[m1][m2].equals(" 鼠1")) {
                                if (!animal[m1 + 1][m2].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 + 1][m2].substring(0, 1)) < 0 && !animal[m1 + 1][m2].substring(0, 1).equals("8")) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1][m2].equals(" 水 ") && animal[m1 + 1][m2].equals("8象 ")) {
                                    System.out.println("水里不能强吃哦~");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals(" 象8")) {
                                if (!animal[m1 + 1][m2].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1 + 1][m2].substring(0, 1).equals("1")) {
                                    System.out.println("快跑！！小老鼠你吃不了啊啊！！");
                                    continue stop;
                                } else if (map[m1 + 1][m2].equals(" 水 ")) {
                                    System.out.println("象不能下水");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else if (animal[m1][m2].equals(" 虎6") || animal[m1][m2].equals(" 狮7")) {
                                if (map[m1 + 1][m2].equals(" 水 ")) {
                                    if (animal[m1 + 1][m2].equals("1鼠 ") || animal[m1 + 2][m2].equals("1鼠 ")) {
                                        System.out.println("你得等等咯！跳不过去哈~");
                                        continue stop;
                                    } else {
                                        if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 + 3][m2].substring(0, 1)) < 0) {
                                            System.out.println("跳过去就死啊啊啊！不行~");
                                            continue stop;
                                        } else if (!animal[m1 + 3][m2].substring(2, 3).equals(" ")) {
                                            System.out.println("友方！！请注意！！");
                                            continue stop;
                                        } else {
                                            animal[m1 + 3][m2] = animal[m1][m2];
                                            animal[m1][m2] = " 　 ";
                                            printLandscape();
                                        }
                                    }
                                } else if (!animal[m1 + 1][m2].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 + 1][m2].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            } else {
                                if (!animal[m1 + 1][m2].substring(2, 3).equals(" ")) {
                                    System.out.println("友方！！请注意！！");
                                    continue stop;
                                } else if (map[m1 + 1][m2].equals(" 水 ")) {
                                    System.out.println(animal[m1][m2].substring(1, 2) + "不能下水");
                                    continue stop;
                                } else if (animal[m1][m2].substring(2, 3).compareTo(animal[m1 + 1][m2].substring(0, 1)) < 0) {
                                    System.out.println("快跑！！你吃不了啊啊！！");
                                    continue stop;
                                } else {
                                    animal[m1 + 1][m2] = animal[m1][m2];
                                    animal[m1][m2] = " 　 ";
                                    printLandscape();
                                }
                            }
                            break;
                    }
                    player += 1;
                    lastStep = player;
                    animalHistory[player] = AnimalChess.copyArray(animal);//记录此时棋盘状态
                } else {
                    System.out.println("\n不能识别指令" + "\"" + s + "\"" + ",请输入help看一下怎么下哈~");
                    continue stop;
                }
            }
        }
    }

    /**
     * This method is to get the animal from the
     * text and give each number a meaning with
     * difference for next operations
     *
     * @return a two-dimensional array named animal
     * @throws FileNotFoundException
     */
    public static String[][] getAnimal() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("animal.txt"));
        String str1l[] = {" 　 ", "1鼠 ", "2猫 ", "3狼 ", "4狗 ", "5豹 ", "6虎 ", "7狮 ", "8象 "};
        String str1r[] = {" 　 ", " 鼠1", " 猫2", " 狼3", " 狗4", " 豹5", " 虎6", " 狮7", " 象8"};
        //根据数字的左右区分一下两边的阵营
        for (int i = 0; i < 7; i++) {
            String a = scanner.nextLine();
            for (int j = 0; j < 3; j++) {
                char animals = a.charAt(j);
                int A = Integer.parseInt("" + animals);
                animal[i][j] = str1l[A];
            }
            for (int j = 3; j < 9; j++) {
                char animals = a.charAt(j);
                int A = Integer.parseInt("" + animals);
                animal[i][j] = str1r[A];
            }
        }
        //把数字转化成字符串，也方便判断阵营
        return animal;
    }

    /**
     * This method is to get the map from the text using loops
     *
     * @return a two-dimensional array named map
     * @throws FileNotFoundException
     */
    public static String[][] getMap() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("tile.txt"));
        String str[] = {" 　 ", " 水 ", "0陷 ", "0穴 ", " 陷0", " 穴0"};
        for (int i = 0; i < 7; i++) {
            String m = scanner.nextLine();
            for (int j = 0; j < 9; j++) {
                char maps = m.charAt(j);
                int M = Integer.parseInt("" + maps);
                map[i][j] = str[M];
            }
        }
        //把数字转化成字符串，也方便判断阵营
        return map;
    }

    /**
     * Returns an integer that is corresponding to
     * the animal's row ,it requires the input string
     * number and then search the array to assure
     *
     * @param m
     * @return the integer of specified row
     */
    public static int assureRow(String m) {
        int a = -1;
        stop:
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (animal[i][j].equals(m)) {
                    a = i;
                    break stop;
                }
            }
        }
        return a;
    }

    /**
     * Returns an integer that is corresponding to
     * the animal's column ,it requires the input string
     * number and then search the array to assure
     *
     * @param m
     * @return the integer of specified column
     */
    public static int assureColumn(String m) {
        int a = -1;
        stop:
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (animal[i][j].equals(m)) {
                    a = j;
                    break stop;
                }
            }
        }
        return a;
    }

    /**
     * This method is to print the whole map
     * with animals covering it, if there is
     * no animal, then print map;else print animal
     */
    public static void printLandscape() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (animal[i][j] != " 　 ") {
                    System.out.print(animal[i][j]);
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }
    }

    /**
     * This method is to print the game rules
     * if users input "help"
     */
    public static void printRules() {
        System.out.println("\n指令介绍：");
        System.out.println();
        System.out.println("1. 移动指令\n  移动指令有两个部分组成。");
        System.out.println("  第一个部分是数字1-8，根据战斗力分别对应鼠（1）" +
                "，猫（2），狼（3），狗（4），豹（5），虎（6），狮（7），象（8）");
        System.out.println("  第二个部分是字母wasd中的一个，w对应上方向，" +
                "a对应左方向，s对应下方向，d对应右方向");
        System.out.println("  比如指令\"1d\"表示鼠向右走，\"4w\"表示狗向左走\n");
        System.out.println("2. 游戏指令");
        System.out.println("  输入 restart 重新开始游戏");
        System.out.println("  输入 help 查看帮助");
        System.out.println("  输入 undo 悔棋");
        System.out.println("  输入 redo 取消悔棋");
        System.out.println("  输入 exit 退出游戏");
    }

    /**
     * Return true or false to judge if the
     * game is over, according to the animal
     * row's value. If not equals -1,then continue
     *
     * @param m
     * @return true or false
     */
    public static boolean isValidInput(String m) {
        if (m.length() != 2) {
            return false;
        } else {
            String m1 = m.substring(0, 1);
            String m2 = m.substring(1, 2);
            if ((m1.equals("1") || m1.equals("2") || m1.equals("3") || m1.equals("4") || m1.equals("5") || m1.equals("6") || m1.equals("7") || m1.equals("8")) && (m2.equals("w") || m2.equals("a") || m2.equals("s") || m2.equals("d"))) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Return true or false to judge if the
     * input is proper and can be read
     *
     * @return true or false
     */
    public static boolean checkHaveAnimals() {
        int m = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (!animal[i][j].substring(1, 2).equals("　")) {
                    if (animal[i][j].substring(0, 1).equals(" ")) {
                        m++;
                    } else if (animal[i][j].substring(2, 3).equals(" ")) {
                        m--;
                    }
                }
            }
        }
        if (m == 8 || m == -8) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns an array duplicating the array in
     * the blanket, this is called deep copy, which
     * can copy the very array totally
     *
     * @param array
     * @return array that is the same to the array inside
     * the blanket
     */
    private static String[][] copyArray(String[][] array) {
        String[][] newArray = new String[7][9];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }
}
