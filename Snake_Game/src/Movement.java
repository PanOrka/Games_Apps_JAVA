class Movement {
    private MyPanel p;

    Movement(MyPanel p) {
        this.p = p;
    }

    void doMove() {
        for (int i=this.p.snakeList.size()-1; i>=0; i--) {
            if (i == 0) {
                this.p.snakeList.get(i).xPos = this.p.snakeHead.xPos;
                this.p.snakeList.get(i).yPos = this.p.snakeHead.yPos;
                this.p.snakeList.get(i).ateFruit = this.p.snakeHead.ateFruit;
                this.p.snakeHead.ateFruit = false;
            } else {
                this.p.snakeList.get(i).xPos = this.p.snakeList.get(i-1).xPos;
                this.p.snakeList.get(i).yPos = this.p.snakeList.get(i-1).yPos;
                this.p.snakeList.get(i).ateFruit = this.p.snakeList.get(i-1).ateFruit;
                this.p.snakeList.get(i-1).ateFruit = false;
            }
        }

        switch(this.p.moveDir) {
                case 'w':
                    this.p.snakeHead.yPos -= 10;
                    break;
                case 's':
                    this.p.snakeHead.yPos += 10;
                    break;
                case 'a':
                    this.p.snakeHead.xPos -= 10;
                    break;
                case 'd':
                    this.p.snakeHead.xPos += 10;
                    break;
        }

        this.p.switchDir = true;

        for (int i=0; i<this.p.snakeList.size(); i++) {
            if (this.p.snakeHead.xPos == this.p.snakeList.get(i).xPos && this.p.snakeHead.yPos == this.p.snakeList.get(i).yPos && this.p.snakeList.size()>=2) {
                this.p.GameOver = true;
                break;
            }
        }

        if (this.p.snakeHead.xPos == 500 || this.p.snakeHead.xPos == -10 || this.p.snakeHead.yPos == -10 || this.p.snakeHead.yPos == 500) {
            this.p.GameOver = true;
        }
    }
}
