import java.util.*;

public class WolfFieldFinder {
private int _x, _y, _sheepX, _sheepY, _wolfX, _wolfY, _howSame, _distance, _rng;
private Owolf _wolf;
private Osheep[] _sheep;
private int[] _whichSheep;
private Random _rand;
private boolean _switcher;

public WolfFieldFinder(Owolf wolf, Osheep[] sheep, int x, int y) {
        this._rand = new Random();
        this._wolf = wolf;
        this._sheep = new Osheep[sheep.length];
        this._x = x;
        this._y = y;

        for (int i=0; i<sheep.length; i++) {
                this._sheep[i] = sheep[i];
        }
}

public int Finder() {
        this._howSame = 1;
        this._switcher = false;

        this._wolfX = ((this._wolf.position) % this._x) + 1;
        this._wolfY = ((this._wolf.position + 1 - this._wolfX)/this._x) + 1;

        for (int i=0; i<this._sheep.length; i++) {
                this._sheepX = ((this._sheep[i].position) % this._x) + 1;
                this._sheepY = ((this._sheep[i].position + 1 - this._sheepX)/this._x) + 1;

                if (!this._sheep[i].isEaten && !this._switcher) {
                        this._distance = this.Distance();
                        this._switcher = true;
                } else if (!this._sheep[i].isEaten && this._distance > this.Distance()) {
                        this._howSame = 1;
                        this._distance = this.Distance();
                } else if (!this._sheep[i].isEaten && this._distance == this.Distance()) {
                        this._howSame++;
                }
        }

        if (this._howSame != 1) {
                this._rng = this._rand.nextInt(this._howSame-1);
                this._whichSheep = new int[this._howSame];

                this._howSame = 0; // TUTAJ POSLUGUJE SIE JAKO MIEJSCEM TABLICY

                for (int i=0; i<this._sheep.length; i++) {
                        this._sheepX = ((this._sheep[i].position) % this._x) + 1;
                        this._sheepY = ((this._sheep[i].position + 1 - this._sheepX)/this._x) + 1;

                        if (!this._sheep[i].isEaten && this.Distance() == this._distance) {
                                this._whichSheep[this._howSame] = i;
                                this._howSame++;
                        }
                }

        } else {
                this._rng = 0;
                this._whichSheep = new int[1];

                for (int i=0; i<this._sheep.length; i++) {
                        this._sheepX = ((this._sheep[i].position) % this._x) + 1;
                        this._sheepY = ((this._sheep[i].position + 1 - this._sheepX)/this._x) + 1;

                        if (!this._sheep[i].isEaten && this.Distance() == this._distance) {
                                this._whichSheep[this._rng] = i;
                                break;
                        }
                }
        }

        this._wolf.sheepFocused = this._whichSheep[this._rng];
        return (int)(this.MoveSelector());
}

private int Distance() {
        int A, B;
        A = this._wolfX - this._sheepX;
        B = this._wolfY - this._sheepY;
        return (int)((A*A)+(B*B));
}

private int MoveSelector() {
        this._sheepX = ((this._sheep[this._whichSheep[this._rng]].position) % this._x) + 1;
        this._sheepY = ((this._sheep[this._whichSheep[this._rng]].position + 1 - this._sheepX)/this._x) + 1;

        if (this._sheepX > this._wolfX) {
                this._wolfX++;
        } else if (this._sheepX < this._wolfX) {
                this._wolfX--;
        }

        if (this._sheepY > this._wolfY) {
                this._wolfY++;
        } else if (this._sheepY < this._wolfY) {
                this._wolfY--;
        }

        return (int)(this._wolfX + ((this._wolfY-1)*this._x) - 1);
}
}
