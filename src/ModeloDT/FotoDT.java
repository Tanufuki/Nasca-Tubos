/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDT;

/**
 *
 * @author Angelo
 */
public class FotoDT {
    
    public int id;
    public int dir1;
    public int dir2;
    public int visible;
    public String ruta;
    
    public FotoDT()
    {
        visible = 1;
        dir1 = 0;
        dir2 = 0;
        ruta = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDir1() {
        return dir1;
    }

    public void setDir1(int dir1) {
        this.dir1 = dir1;
    }

    public int getDir2() {
        return dir2;
    }

    public void setDir2(int dir2) {
        this.dir2 = dir2;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    
    
    
}
