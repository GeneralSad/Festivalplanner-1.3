package Simulator.Pathfinding;

import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

public class Pathfinding
{
    private ArrayList<ArrayList<PathfindingTile>> pathfindingtiles;
    private int tileWidth;
    private int tileHeight;
    private int totalWidth;
    private int totalHeight;
    private PathfindingTile destinationTile;
    private ArrayList<NPC> npcs;

    public Pathfinding(TiledMap tiledMapBase)
    {
        this(tiledMapBase.getTileWidth(), tiledMapBase.getTileHeight(), tiledMapBase.getWidth(), tiledMapBase.getHeight());
    }

    public Pathfinding(int tileWidth, int tileHeight, int totalWidth, int totalHeight)
    {
        this.npcs = new ArrayList<>();
        this.pathfindingtiles = new ArrayList<>();
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        init();
    }

    public void addNpc(NPC npc) {
        this.npcs.add(npc);
    }

    public void addNpcs(ArrayList<NPC> npcs) {
        this.npcs.addAll(npcs);
    }

    public void removeNpc(NPC npc) {
        this.npcs.remove(npc);
    }

    /**
     * Initialise the list of all pathfinding tiles
     */
    private void init() {
        for (int row = 0; row < totalHeight; row++) {
            ArrayList<PathfindingTile> columnList = new ArrayList<>();
            for (int column = 0; column < totalWidth; column++) {
                columnList.add(new PathfindingTile(row, column, tileWidth, tileHeight, true));
            }
            this.pathfindingtiles.add(columnList);
        }
    }

    public void setDestination(int x, int y) {
        int row = y / tileHeight;
        int column = x / tileWidth;
        PathfindingTile tile = getTile(row, column);
        if (tile != null) {
            destinationTile = tile;
            // set the exact destination, so the npc doesn't just stop at the edge of the tile
            destinationTile.setExactDestination(new Point2D.Double(x, y));
            calculatePaths(destinationTile);
        }

        for (NPC npc : npcs) {
            npc.resetDestination();
        }
    }

    /**
     * Calculate the pathfinding to go to a destination tile from each other tile on the map
     * @param destination
     */
    private void calculatePaths(PathfindingTile destination) {
        HashSet<PathfindingTile> tilesToCheck = new HashSet<>();
        tilesToCheck.add(destination);
        destination.setDirection(Direction.NONE);

        HashSet<PathfindingTile> tilesAlreadyChecked = new HashSet<>();

        // Keep on checking untill there are no new tiles to check
        while (!tilesToCheck.isEmpty()) {
            HashSet<PathfindingTile> newTilesToCheck = new HashSet<>();
            for (PathfindingTile origin : tilesToCheck) {
                int fromRow = origin.getRow();
                int fromColumn = origin.getColumn();

                // Take each directly adjacent tile and if they aren't outside of the map or unwalkable then set their direction towards the originating tile
                // Then also add the tile to a set of new tiles to check

                PathfindingTile left = getTile(fromRow, fromColumn - 1);
                if (left != null && left.isWalkable() && !tilesAlreadyChecked.contains(left)) {
                    newTilesToCheck.add(left);
                    left.setDirection(Direction.RIGHT);
                }

                PathfindingTile right = getTile(fromRow, fromColumn + 1);
                if (right != null && right.isWalkable() && !tilesAlreadyChecked.contains(right)) {
                    newTilesToCheck.add(right);
                    right.setDirection(Direction.LEFT);
                }

                PathfindingTile up = getTile(fromRow - 1, fromColumn);
                if (up != null && up.isWalkable() && !tilesAlreadyChecked.contains(up)) {
                    newTilesToCheck.add(up);
                    up.setDirection(Direction.DOWN);
                }

                PathfindingTile down = getTile(fromRow + 1, fromColumn);
                if (down != null && down.isWalkable() && !tilesAlreadyChecked.contains(down)) {
                    newTilesToCheck.add(down);
                    down.setDirection(Direction.UP);
                }

                tilesAlreadyChecked.add(origin);
            }

            tilesToCheck = newTilesToCheck;
        }
    }

    public PathfindingTile getTile(int row, int column) {
        if (row >= 0 && row < totalHeight && column >= 0 && column < totalWidth) {
            return this.pathfindingtiles.get(row).get(column);
        }
        return null;
    }

    public PathfindingTile getDestinationTile()
    {
        return destinationTile;
    }

    public int getTileWidth()
    {
        return tileWidth;
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public void draw(FXGraphics2D fxGraphics2D, boolean debug) {
        for (ArrayList<PathfindingTile> row : this.pathfindingtiles) {
            for (PathfindingTile tile : row) {
                tile.draw(fxGraphics2D, debug);
            }
        }
    }
}
