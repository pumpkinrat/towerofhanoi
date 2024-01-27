/*
 * Hanoi.java textbased Tower of Hanoi game
 * 
 * Copyright 2024 Pumpkinrat
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */

// ...................................
// ...............##-##...............
// ...................................
// .....|...........|...........|.....
// .....|...........|...........|.....
// ....#|#..........|...........|.....
// .####|####....###|###........|.....
// /////a///////////b///////////c/////

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Hanoi{    
    ////////////////////////////////////////////////////////////////////
    private static int N_bricks;
    private static int N_towers;
    private static int W_scrn;
    private static int H_scrn;
    private static boolean loop_game;
    
    public static BufferedReader inpt;
    private static String scene_screen[][];
    private static String buff_screen[][];
    private static BrickStack towers[];
    
    ////////////////////////////////////////////////////////////////////
    static String read_string() {
        try { 
            return inpt.readLine();
        } catch (IOException e) {
            throw new RuntimeException ("INPUT ERROR!");
        } 
    }
    
    private static void put_on_screen(int x, int y, String s) {
        if ((x>-1)&&(x<W_scrn)&&(y>-1)&&(y<H_scrn)) {
            buff_screen[y][x] = s;
        }
    }
    
    ////////////////////////////////////////////////////////////////////
    private static class BrickStack {
        private int h;
        private int h_max;
        private int[] arr;
        public BrickStack(int hight) {
            h = -1;
            h_max = hight;
            arr = new int[h_max];
        }
        public boolean isEmpty() {
            return (h<0);
        }
        public boolean isFull() {
            return (h==(h_max-1));
        }
        public void push(int w_brick) {
            h++;
            arr[h] = w_brick;
        }
        public int pop(){
            if (h>-1) {
                int res = arr[h];
                arr[h] = 0;
                h--;
                return res;
            } else {
                return -1;
            }
        }
        public int peek() {
            if (h<0) {
                return -1;
            } else {
                return arr[h];
            }
        }
        public void draw(int offset) {
            int yoff = H_scrn-N_bricks-2;
            for (int i=(h_max-1); i>-1; i--) {
                for (int j=0; j<arr[i]; j++) {
                    put_on_screen( (offset-1-j),(h_max-i+yoff),"#");
                    put_on_screen( (offset+1+j),(h_max-i+yoff),"#");
                }
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////
    private static void init_scene() {
        for (int i=0; i<H_scrn; i++) {
            for (int j=0; j<W_scrn; j++) {
                if (i==(H_scrn-1)) {
                    if (((j-1-N_bricks)%(2*N_bricks+4))==0) {
                        scene_screen[i][j] = Character.toString( (char) ( (j-4)/(2*N_bricks+4) +97 ) );
                    } else {
                        scene_screen[i][j] = "/";
                    }
                } else if ( (i>2) && (((j-1-N_bricks)%(2*N_bricks+4))==0) ) {
                    scene_screen[i][j] = "|";
                } else {
                    scene_screen[i][j] = ".";
                }
            }
        }
    }
    
    private static void draw_scene() {
        for (int i=0; i<H_scrn; i++) {
            for (int j=0; j<W_scrn; j++) {
                put_on_screen(j,i,scene_screen[i][j]);
            }
        }
    }
    
    private static void draw_bricks() {
        for (int i=0; i<N_towers; i++) {
            towers[i].draw( (N_bricks+1+(i*(2*N_bricks+4))) );
        }
    }
    
    public static void draw_mybrick(int w_brick) {
        if (w_brick>0) { put_on_screen( (W_scrn/2), 1, "-");}
        for (int i=1; i<=w_brick; i++) {
            put_on_screen( ((W_scrn/2)-i), 1,"#");
            put_on_screen( ((W_scrn/2)+i), 1,"#");
        }
    }
    
    private static void display() {
        for (int i=0; i<H_scrn; i++) {
        System.out.printf(" ");
        for (int j=0; j<W_scrn; j++) {
            System.out.printf("%s",buff_screen[i][j]);
        }
        System.out.printf("%n");
    }
}

////////////////////////////////////////////////////////////////////
    private static void game_level(int level) {
        N_bricks = 3 + level -1;
        W_scrn = (((N_bricks*2)+1)*N_towers)+(3*(N_towers-1))+2;
        H_scrn = N_bricks+4;
        
        for (int i=0; i<N_towers; i++) {
            towers[i] = new BrickStack(N_bricks);
        }
        
        for (int w_brick=N_bricks; w_brick>0; w_brick--) {
            towers[0].push(w_brick);
        }
        
        String userinput = "";
        String log_msg = "*** Level "+String.valueOf((level))+" ***";
        int selected = -1;
        int curr_brick = -1;
        int ground_brick;
        boolean loop_level = true;
        init_scene();
        while (loop_level) {
            draw_scene();
            draw_bricks();
            draw_mybrick(curr_brick);
            System.out.printf("\033[H\033[J");
            System.out.println("");
            System.out.println(" Options: 'a', 'b', 'c', 'exit'");
            display();
            System.out.println(" "+(log_msg));
            System.out.printf(" > ");
            userinput = read_string();
            
            if (userinput.equals("exit")) {
                loop_level = false;
                loop_game = false;
            } else if (userinput.length()>0) {
                selected = (int) userinput.charAt(0) - 97;
                if ( (selected<0) || (selected>=(N_towers)) ) {
                    selected = -1;
                }
            }
            
            if ( (curr_brick==-1) && (selected!=-1) ) {
                curr_brick = towers[selected].pop();
                log_msg = "+ picked up brick from "+Character.toString((selected+97))+".";
            } else if ( (curr_brick!=-1) && (selected!=-1) ) {
                ground_brick = towers[selected].peek();
                if ( (ground_brick>curr_brick) || (ground_brick==-1) ) {
                    towers[selected].push(curr_brick);
                    curr_brick = -1;
                    log_msg = "+ placed brick on "+Character.toString((selected+97))+".";
                    if ( (selected!=0) && (towers[selected].isFull()) ) {
                        loop_level = false;
                    }
                } else {
                    log_msg = "x can't place on smaller brick!";
                }
            }
        }
    }
    
    private static void game() {
        for (int level=0; (level<(64-3))&&(loop_game); level++) {
            game_level(level+1);
        }
    }
    
    private static void splash_menu() {
        boolean loop_splash = true;
        String userinput;
        while (loop_splash) {
            System.out.printf(" _   _    _    _   _  ___ ___ %n| | | |  / \\  | \\ | |/ _ \\_ _|%n| |_| | / _ \\ |  \\| | | | | | %n|  _  |/ ___ \\| |\\  | |_| | | %n|_| |_/_/   \\_\\_| \\_|\\___/___|%n");
            System.out.printf("%n         > ENTER <");
            userinput = read_string();
            loop_splash = false;
            game();
        }
    }
    
    private static void version() {
        System.out.printf("Hanoi 0.0 Tower of Hanoi game.%nCopyright (C) 2024 Pumpkinrat%nLicense GPLv3+: GNU GPL version 3 or later <https://gnu.org/licenses/gpl.html>.%nThis is free software: you are free to change and redistribute it.%nThere is NO WARRANTY, to the extent permitted by law.%n%nWritten by Pumpkinrat.");
    }
    
    private static void help() {
        System.out.printf(" -h, --help \t\t this help Page%n -V, --version \t\t Version information%n -l, -level <number> \t play level number 1 to 64 ");
    }
    
    ////////////////////////////////////////////////////////////////////
    public static void main (String[] args) {
        inpt = new BufferedReader (new InputStreamReader (System.in));
        N_towers = 3;
        N_bricks = 3;
        buff_screen = new String[70][407];
        scene_screen = new String[70][407];
        towers = new BrickStack[N_towers];
        if (args.length>0) {
            switch (args[0]) {
                case "--help": help(); break; 
                case "--version": version(); break; 
                case "-V": version(); break;
                case "-l": ;
                case "--level":
                    if (args.length>1) {
                        try {
                            int num = Integer.parseInt(args[1]);
                            game_level(num);
                        } catch (NumberFormatException e) {
                            System.out.println("can not parse number!");
                            break;
                        } 
                    } else {
                            System.out.println("too few arguments!");
                    }
                    break;
                default: help();
            }
        } else {
            loop_game = true;
            splash_menu();
        }
        System.out.println("");
    }
}
