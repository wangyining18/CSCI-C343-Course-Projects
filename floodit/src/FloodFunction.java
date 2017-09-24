
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.*;

public class FloodFunction {
    public Driver driver;
    public List<Coord> flooded_list = new LinkedList<>();
    public Map<Coord, Boolean> visited = new HashMap();
    public Map<Coord, Boolean> centered = new HashMap();
    public FloodFunction(final Driver _driver) {
        this.driver = _driver;
        this.flooded_list.add(new Coord(0, 0));
    }

    public boolean inbound(final Coord coord) {
        return coord.x > -1 && coord.x < this.driver.size && coord.y > -1 && coord.y < this.driver.size;
    }

    public Coord up(final Coord coord) {
        return new Coord(coord.x, coord.y-1);
    }

    public Coord down(final Coord coord) {
        return new Coord(coord.x, coord.y+1);
    }

    public Coord left(final Coord coord) {
        return new Coord(coord.x-1, coord.y);
    }

    public Coord right(final Coord coord) {
        return new Coord(coord.x+1, coord.y);
    }

    public void flood(final Map color_of_tiles, final Integer color) {
        for(int i = 0; i < flooded_list.size(); i++){
            Coord current = flooded_list.get(i);
            Coord neighbor = up(current);
            if( !flooded_list.contains(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
            }
            neighbor = down(current);
            if( !flooded_list.contains(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
            }
            neighbor = left(current);
            if( !flooded_list.contains(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
            }
            neighbor = right(current);
            if( !flooded_list.contains(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
            }
        }
    }

    public void flood1(final Map color_of_tiles, final Integer color) {
        for(int i = 0; i < flooded_list.size(); i++){
            Coord current = flooded_list.get(i);
            Coord neighbor = up(current);
            if( !visited.containsKey(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
                visited.put(neighbor, true);
            }
            neighbor = down(current);
            if( !visited.containsKey(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
                visited.put(neighbor, true);
            }
            neighbor = left(current);
            if( !visited.containsKey(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
                visited.put(neighbor, true);
            }
            neighbor = right(current);
            if( !visited.containsKey(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
                visited.put(neighbor, true);
            }
        }
    }

    public void flood2(final Map color_of_tiles, final Integer color) {
        int isCentered= 0;
        for(int i = 0; i < flooded_list.size(); i++){
            Coord current = flooded_list.get(i);
            if(centered.containsKey(current)){
                continue;
            }
            isCentered = 0;
            Coord neighbor = up(current);
            if( !visited.containsKey(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
                visited.put(neighbor, true);
                isCentered ++;
            }
            neighbor = down(current);
            if( !visited.containsKey(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
                visited.put(neighbor, true);
                isCentered ++;
            }
            neighbor = left(current);
            if( !visited.containsKey(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
                visited.put(neighbor, true);
                isCentered ++;
            }
            neighbor = right(current);
            if( !visited.containsKey(neighbor) && color_of_tiles.get(neighbor) == color &&  inbound(neighbor) ){
                flooded_list.add(neighbor);
                visited.put(neighbor, true);
                isCentered ++;
            }
            if(isCentered == 4){
                centered.put(current, true);
            }
        }
    }
}
