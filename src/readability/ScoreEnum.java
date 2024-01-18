package readability;

enum ScoreEnum {
    KINDERGARTEN(1,"5-6"),

    FIRST_GRADE(2,"6-7"),

    SECOND_GRADE(3,"7-8"),

    THIRD_GRADE(4,"8-9"),

    FOURTH_GRADE(5,"9-10"),

    FIFTH_GRADE(6,"10-11"),

    SIXTH_GRADE(7,"11-12"),

    SEVENTH_GRADE(8,"12-13"),

    EIGHT_GRADE(9,"13-14"),

    NINTH_GRADE(10,"14-15"),

    TENTH_GRADE(11,"15-16"),

    ELEVENTH_GRADE(12,"16-17"),

    TWELFTH_GRADE(13,"17-18"),

    COLLEGE_STUDENT(14,"18-22");

    int score;
    String age;

    ScoreEnum(int score, String age) {
        this.score = score;
        this.age = age;
    }

    static ScoreEnum getByScore(int score){
        if (score > COLLEGE_STUDENT.score){
            return COLLEGE_STUDENT;
        }

        for (ScoreEnum theScore : ScoreEnum.values()){
            if (theScore.score == score){
                return theScore;
            }
        }

        return null;
    }
}

