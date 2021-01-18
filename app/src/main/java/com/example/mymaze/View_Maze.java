package com.example.mymaze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class View_Maze extends AppCompatImageView {
    private int screenWidth;
    private int mWidth,mHeight;
    int map[][] = new int[200][200];
    int width=18,height=20,xP=1,yP=1;
    int xPath[]=new int[100],yPath[]=new int[100];
    String Path[]=new String[100];
    String resultPath;
    Bitmap bitmap_wall = BitmapFactory.decodeResource(getResources(),R.mipmap.wall);
    Bitmap bitmap_path =BitmapFactory.decodeResource(getResources(),R.mipmap.path);


    public View_Maze(Context context){
        super(context);
    }
    public View_Maze(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public View_Maze(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void CreateMaze(int map[][],int width,int height){
        for(int i=0;i<height+2;i++){
            for(int j=0;j<width+2;j++){
                if(i==0 || j==0 || i==height+1 || j==width+1){
                    map[i][j]=0;
                }else{
                    int tmp=(int)(Math.random()*10000%30);
                    if(tmp<15){
                        map[i][j]=0;
                    }else{
                        map[i][j]=1;
                    }
                }
            }
        }
        map[1][1]=1;
        int i=1,j=1;
        for(int k=0;k<36;k++){
            if(i<height && j<width){
                int dir = (int)(Math.random()*1000%2);
                switch (dir){
                    case 0://向下移动
                        i = i+1;
                        break;
                    case 1:
                        j = j+1;
                        break;
                }
                map[i][j]=1;
            }else if(i==height && j<width){
                j=j+1;
                map[i][j]=1;
            }else if(i<height && j==width){
                i=i+1;
                map[i][j]=1;
            }
            map[height][width]=1;
        }
    }
    public void findPath(int map[][],int width,int height,String Path[],String resultPath,int xPath[],int yPath[]){
        int n=height+2;//height
        int m=width+2;//width
        int x1=1,y1=1;
        int [][]visited=new int[n][m];
        int [][]fa=new int[n][m];		//记录到该节点的上一个节点的坐标
        int [][]last_dir=new int[n][m];
        int []dx={-1,1,0,0};			//四个方向
        int []dy={0,0,-1,1};
        char []dir={'U','D','L','R'};
        for(int i=0;i<n;i++){
            Arrays.fill(visited[i], 0);
        }
        int src_x=1;	//起点
        int src_y=1;
        int des_x=20;	//终点
        int des_y=18;
        Queue<Integer> queue=new LinkedList<Integer>();
        queue.offer(src_x*m+src_y);			//矩阵数组按0,1,2...n*m编号
        fa[src_x][src_y]=src_x*m+src_y;
        visited[src_x][src_y]=1;
        while(!queue.isEmpty()){
            int index=queue.poll();
            int x=index/m;
            int y=index%m;
            for(int i=0;i<4;i++){
                int nx=x+dx[i];
                int ny=y+dy[i];
                if(nx>=0&&nx<n&&ny>=0&&ny<m&&map[nx][ny]==1&&visited[nx][ny]==0){
                    queue.offer(nx*m+ny);
                    visited[nx][ny]=1;
                    fa[nx][ny]=index;
                    last_dir[nx][ny]=i;
                }
            }
        }
        StringBuffer path=new StringBuffer();
        int fx=des_x;
        int fy=des_y;
        int index=des_x*m+des_y;
        while(fa[fx][fy]!=index){
            path.append(dir[last_dir[fx][fy]]);
            int x=fa[fx][fy]/m;
            int y=fa[fx][fy]%m;
            fx=x;
            fy=y;
            index=fx*m+fy;
        }
        String Di=path.reverse().toString();
        Di=Di+"N";
        char up = 'U';
        char down = 'D';
        char  left = 'L';
        char right = 'R';
        char di[]=Di.toCharArray();
        Path[0]="(1,1"+di[0]+")";
        Path[di.length]="(20,18"+di[di.length-1]+")";
        for(int i=0;i<di.length;i++) {
            if (di[i] == up) {
                x1=x1;
                y1=y1-1;
                xPath[i+1]=x1;
                yPath[i+1]=y1;
                Path[i+1]="("+y1+","+x1+","+di[i+1]+")";
            } else if(di[i]==down) {
                x1=x1;
                y1=y1+1;
                xPath[i+1]=x1;
                yPath[i+1]=y1;
                Path[i+1]="("+y1+","+ x1+","+di[i+1]+")";
            } else if (di[i]==left) {
                x1=x1-1;
                y1=y1;
                xPath[i+1]=x1;
                yPath[i+1]=y1;
                Path[i+1]="("+y1+","+x1+","+di[i+1]+")";
            } else if (di[i]==right) {
                x1=x1+1;
                y1=y1;
                xPath[i+1]=x1;
                yPath[i+1]=y1;
                Path[i+1]="("+y1+","+x1+","+di[i+1]+")";
            }
        }
        xPath[0]=1;yPath[0]=1;
        for(int i=0;i<37;i++){
            xP=xPath[i];
            yP=yPath[i];
            map[yP][xP]=2;
        }
        resultPath=Path[0];
        for(int i=1;i<di.length;i++){
            resultPath=resultPath+Path[i];
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = View.MeasureSpec.getSize(widthMeasureSpec);
        mWidth = specSize;
        specSize = View.MeasureSpec.getSize(heightMeasureSpec);
        mHeight = specSize;
        setMeasuredDimension(mWidth, mHeight);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        CreateMaze(map,width,height);
        findPath(map,width,height,Path,resultPath,xPath,yPath);
        Paint p=new Paint();

        p.setColor(Color.RED);
        for(int i=0;i<height+2;i++){
            for(int j=0;j<width+2;j++){
                Rect rect = new Rect((mWidth/20)*j,(mWidth/20)*i,(mWidth/20)*(j+1),
                        (mWidth/20)*(i+1));
                int x=map[i][j];
                switch (x){
                    case 0:
                        canvas.drawBitmap(bitmap_wall,null,rect,p);
                        break;
                    case 2:
                        canvas.drawBitmap(bitmap_path,null,rect,p);
                        break;
                    default:
                        break;
                }
            }
        }
        Paint paint=new Paint(screenWidth);
        Point point = new Point();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        paint.setTextSize(40);
        //canvas.drawText(Path[0],1,1350,paint);
        int x=1,y=1750;
        for(int i=0;i<36;i++){
            canvas.drawText(Path[i]+"->",x,y,paint);
            x+=170;
            if((i+1)%8==0){
                x=1;
                y+=100;
            }

        }
        canvas.drawText(Path[36],x,y,paint);

    }
}
