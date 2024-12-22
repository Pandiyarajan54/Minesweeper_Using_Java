import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Minesweeper {

    private class MineTile extends JButton{
        int r,c;

        public MineTile(int r,int c){
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 70;
    int numRows = 8;
    int numCols = 8;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;
    int mineCount = 8;
    Random random = new Random();


    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;

    int tilesClicked = 0;
    boolean gameOver = false;




    Minesweeper()
    {
        frame.setSize(boardWidth,boardHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        textLabel.setFont(new Font("Arial", Font.BOLD,25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel,BorderLayout.NORTH);


        boardPanel.setLayout(new GridLayout(numRows,numCols));
        frame.add(boardPanel);

        for(int r=0;r<numRows;r++)
        {
            for(int c=0;c<numCols;c++)
            {
                MineTile tile = new MineTile(r,c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setBackground(Color.black);
                tile.setForeground(Color.white);
                tile.setMargin(new Insets(0,0,0,0));
                tile.setPreferredSize(new Dimension(50,50));
                tile.setFont(new Font("Arial Unicode MS",Font.PLAIN,40));
                tile.setText("");
                tile.setHorizontalAlignment(SwingConstants.CENTER);

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {

                        if(gameOver)
                            return;

                        MineTile tile = (MineTile) e.getSource();

                        //for left click
                        if(e.getButton() == MouseEvent.BUTTON1)
                        {
                            if(tile.getText() == ""){

                                if(mineList.contains(tile)){
                                    try {
                                        revealMines();
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                                else {

                                    checkMine(tile.r,tile.c);
                                    tile.setBackground(Color.WHITE);
                                }
                            }
                        }

                    }
                });

                boardPanel.add(tile);

            }

        }


        frame.setVisible(true);
        setMines();


    }

    void setMines(){

        mineList = new ArrayList<MineTile>();

       int mineLeft = mineCount;

       while(mineLeft > 0)
       {
           int r = random.nextInt(numRows);
           int c = random.nextInt(numCols);

           MineTile tile = board[r][c];
           if(!mineList.contains(tile)){

               mineList.add(tile);
               mineLeft -= 1;
           }
       }
    }


    void revealMines() throws IOException {

        for(int i =0;i< mineList.size();i++) {

            MineTile tile= mineList.get(i);
            tile.setText("×");
            tile.setBackground(Color.WHITE);
            tile.setForeground(Color.darkGray);

        }

        gameOver = true;
        textLabel.setText("Game Over !!");


    }

    void checkMine(int r,int c){

        if(r < 0 || r >=numRows || c < 0 || c >= numCols)
            return ;

        MineTile tile = board[r][c];

        if(!tile.isEnabled())
            return;

        tile.setEnabled(false);
        tile.setBackground(Color.WHITE);

        int minesFound = 0;
        tilesClicked  += 1;




        // top 3
        minesFound += countMines(r-1,c-1); //top left
        minesFound += countMines(r-1,c); //top
        minesFound += countMines(r-1,c+1); //top right


        //left and right
        minesFound  += countMines(r,c-1);//left
        minesFound  += countMines(r,c+1);//right


        //bottom 3
        minesFound += countMines(r+1,c-1); //bottom left
        minesFound += countMines(r+1,c); //bottom
        minesFound += countMines(r+1,c+1); //bottom right

        if(minesFound > 0)
            tile.setText(Integer.toString(minesFound));

        else {
            tile.setText("");

            //top 3
            checkMine(r-1,c-1); //top left
            checkMine(r-1,c); //top
            checkMine(r-1,c+1); //top right

            //left and right
            checkMine(r,c-1); //left
            checkMine(r,c+1); //right

            //bottom 3
            checkMine(r+1,c-1); //bottom left
            checkMine(r+1,c); //bottom
            checkMine(r+1,c+1); //bottom right

        }


        if(tilesClicked == numRows * numCols - mineList.size())
        {
            gameOver = true;
            textLabel.setText("Mines are cleared !!");
        }


    }

    int countMines (int r,int c){

        if(r < 0 || r >=numRows || c < 0 || c >= numCols)
            return 0;

        if(mineList.contains(board[r][c]))
            return 1;

        return 0;


    }




}
