module analysis.precision {
    requires confusion.matrix.api;


    exports pl.e_science.git.precision;

    provides pl.e_science.git.ivahan0788.AnalysisService
            with pl.e_science.git.precision.PrecisionService;
}
