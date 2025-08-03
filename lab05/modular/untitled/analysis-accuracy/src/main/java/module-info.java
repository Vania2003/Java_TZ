module analysis.accuracy {
    requires confusion.matrix.api;

    exports pl.e_science.git.accuracy;

    provides pl.e_science.git.ivahan0788.AnalysisService
            with pl.e_science.git.accuracy.AccuracyService;
}
