package world;

import world.tile.Glass;
import world.tile.Stone;
import world.tile.Tile;
import world.tile.Void;
import world.tile.Wall;
import world.tile.Water;
import world.tile.WoodPlank;

public class DebugRoomMaker {
	private static String[][] prebuilt = new String[][]{{"################################################################################",
														 "#......................wwwwwwwwwwww............................................#",
														 "#......................#wwwwwwwwww#............................................#",
														 "#......................pppppppppppp............................................#",
														 "#......................#wwwwwwwwww#............................................#",
														 "#.......####...........wwwwwwwwwwww............................................#",
														 "#    ..    #...........wwwwwww########.........................................#",
														 "#    ..ppppp............wwwwww#      #................  #..#  .................#",
														 "     ..ppppp.............wwwww#      #................  #..#  .................#",
														 "     ..    #...............www########...............  #..#  ..................#",
														 "#.......####.....gggg.......wwww..................... #..#  ...................#",
														 "#...............gggggg..................................#  ....................#",
														 "#...............gg  gg.........................................................#",
														 "#...............gg  gg....................................######################",
														 "#...............gggggg....................................#gg#p#g####......#...#",
														 "#................gggg.......wwwwg     ..     .............####p#gggg#......###g#",
														 "#...#..#....................wwwwg            .............#pppp######..........#",
														 "    g..g     ...............wwwwg       ..................#ppppppppp#..........#",
														 "    g..g     ...............ggg.g          ...............#ppppppppp#..........#",
														 "    g..g     ....................             ............gpppppppppp..........#",
														 "    gggg     .............................................gpppppppppp..........#",
														 "             ###################################################################"},
														{"gggggggggggggggg",
														 "ggwwwwwwwwwggggg",
														 "ggwww####wwwgggg",
														 "gggwwwwwwwwwwwgg",
														 "gggggggggggggggg"},
														{"wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",
														 "wwwwwwwwwwwwwwwwwwwwwww",}};
									 
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
					map[i][j] = new WoodPlank(i,j,caller);
					break;
				default:
					map[i][j] = new Void(i,j,caller);
				}
			}
		
		return map;
	}
}
