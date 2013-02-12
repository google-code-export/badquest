package world;

import world.tile.Dirt;
import world.tile.Glass;
import world.tile.Lava;
import world.tile.Smart;
import world.tile.Spring;
import world.tile.Stone;
import world.tile.Tile;
import world.tile.Void;
import world.tile.Wall;
import world.tile.Water;
import world.tile.WoodPlankH;
import world.tile.WoodPlankV;

public class DebugRoomMaker {
	private static String[][] prebuilt = new String[][]{{"################################################################################",
														 "#............sssssssss.wwwwwwwwwwww............#.....#..#.....#.#..............#",
														 "#............sssssssss.#wwwwwwwwww#............#..#..#..#.#.#......kpkpkpkpkpk.#",
														 "#............sssssssss.pppppppppppp............#..#...........#.###pkpkpkpkpkp.#",
														 "#............sssssssss.#wwwwwwwwww#............#..##########.##.#..kpkpkpkpkpk.#",
														 "#.......####.sssssssss.wwwwwwwwwwww........................#.......pkpkpkpkpkp.#",
														 "#   kkbbbbb#.sssssssss.wwwwwww########.....................#.......kpkpkpkpkpk.#",
														 "#   kkpppppp............wwwwww#      #................  #..#  .................#",
														 "#   kkpppppp.............wwwww#      #................  #..#  .................#",
														 "#   kklllll#...............www########...............  #..#  ..................#",
														 "#ddddddd####.....gggg.......wwww..................... #..#  ...................#",
														 "#ddddddddddd....ggllgg..................................#  ....................#",
														 "#dddddddddd.....gllllg.........................................................#",
														 "#ddddddddd......gllllg............. ......................######################",
														 "#dddddddd.......ggllgg.............  .....................#gg#p#g####......#...#",
														 "#ddddddd.........gggg....bbbbbbbg     ..     .............####p#gggg#......###g#",
														 "#...#d.#................bb.bbbggg            .............#pppp######..........#",
														 "#   g..g     .........bbb..bbbgbg       ..................#ppppppppp#..........#",
														 "#   g..g     ........bb...bbggg.g          ...............#ppppppppp#..........#",
														 "#   g..g     .......bbbbbbbbb....             ............gpppppppppp..........#",
														 "#   gggg     ........bb..bbb.............. ...............gpppppppppp..........#",
														 "################################################################################"},
														{"gggggggggggggggg",
														 "ggwwwwwwwwwggggg",
														 "ggwww####wwwgggg",
														 "gggwwwwwwwwwwwgg",
														 "gggggggggggggggg"},
														{"ssssssssssssssss",
														 "sswwwwwwwwwsssss",
														 "sswww####wwwssss",
														 "ssswwwwwwwwwwwss",
														 "ssswwsssswwsssss",
														 "ssswwsssswwsssss",
														 "ssswwwwwwwwsssss",
														 "ssswwwwwwwwsssss",
														 "ssssssssssssssss"},
														{"wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",},
														{"##########",
														 "#........#",
														 "#........#",
														 "#........#",
														 "#........#",
														 "##########"},
														{"pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
														 "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp"},
														 {"       ######       ",
														  "     ##......##     ",
														  "   ##..........##   ",
														  "  #..............#  ",
														  "  #..............#  ",
														  " #................# ",
														  " #................# ",
														  "#..................#",
														  "#..................#",
														  "#..................#",
														  "#..................#",
														  "#..................#",
														  "#..................#",
														  " #................# ",
														  " #................# ",
														  "  #..............#  ",
														  "  #..............#  ",
														  "   ##..........##   ",
														  "     ##......##     ",
														  "       ######       ",}};
									 
	public static Tile[][] selectPrebuilt(int s, Room caller){
		return make(prebuilt[s], caller);
	}
	
	public static Tile[][] make(String[] in, Room caller){
		int R = in.length;
		int C = in[0].length();
		Tile[][] map = new Tile[R][C];
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++){
				switch(in[i].charAt(j)){
				case '#':
					map[i][j] = new Wall(i,j,caller);
					break;
				case 'd':
					map[i][j] = new Dirt(i,j,caller);
					break;
				case '.':
					map[i][j] = new Stone(i,j,caller);
					break;
				case 'g':
					map[i][j] = new Glass(i,j,caller);
					break;
				case 'w':
					map[i][j] = new Water(i,j,caller);
					break;
				case 'p':
					map[i][j] = new WoodPlankH(i,j,caller);
					break;
				case 'k':
					map[i][j] = new WoodPlankV(i,j,caller);
					break;
				case 's':
					map[i][j] = new Smart(i,j,caller);
					break;
				case 'l':
					map[i][j] = new Lava(i,j,caller);
					break;
				case 'b':
					map[i][j] = new Spring(i,j,caller);
					break;
				default:
					map[i][j] = new Void(i,j,caller);
				}
			}
		
		return map;
	}
	
	public static int prebuiltRows(int s){
		return prebuilt[s].length;
	}
	
	public static int prebuiltCols(int s){
		return prebuilt[s][0].length();
	}
}
