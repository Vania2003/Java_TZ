module analysis.kappa {
    requires confusion.matrix.api;


    exports pl.e_science.git.kappa;

    provides pl.e_science.git.ivahan0788.AnalysisService
            with pl.e_science.git.kappa.KappaService;
}
