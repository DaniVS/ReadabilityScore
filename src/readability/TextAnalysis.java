package readability;

class TextAnalysis {

    private int words;
    private int sentences;
    private int characters;
    private int syllables;
    private int polysyllables;

    private float score;
    private String age;

    TextAnalysis() {
        this.words = 0;
        this.sentences = 0;
        this.characters = 0;
        this.syllables = 0;
        this.polysyllables = 0;
        this.score = 0;

    }

    private void incrementWords() {
        this.words++;
    }

    void incrementWordsBy(int increment) {
        this.words += increment;
    }

    void incrementSentences() {
        this.sentences++;
    }

    private void incrementCharacters() {
        this.characters++;
    }

    void incrementCharactersBy(int increment) {
        this.characters += increment;
    }

    void incrementSyllablesBy(int syllables){
        this.syllables += syllables;
    }

    void incrementPolysyllables(int syllables){
        if (syllables > 2){
            this.polysyllables++;
        }
    }
    public int getWords() {
        return words;
    }

    public int getSentences() {
        return sentences;
    }

    public int getCharacters() {
        return characters;
    }

    public float getScore() {
        return score;
    }

    public String getAge() {
        return ScoreEnum.getByScore((int) Math.ceil(this.score)).age;
    }

    public int getSyllables() {
        return syllables;
    }

    public int getPolysyllables() {
        return polysyllables;
    }

    public void setScore(float score){
        this.score = score;
    }
}

