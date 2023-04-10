package us.abstracta.jmeter.javadsl.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

import java.io.IOException;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;

public class ConsultasAMultas{

  @Test
  public void testPerformance() throws IOException {
    TestPlanStats stats = testPlan(
        httpCookies(),
        threadGroup("consultas", 1, 1)
                .children(
                        httpSampler(baseClass.URL)
                                .method("GET")
                                .protocol("https")
                                .header("Host",baseClass.URL)
                                .header("Connection","keep-alive"),
                        responseAssertion("<h1 class='text-center'>Pr&oacute;ximos vencimientos de patente</h1>")
                )
                .children(
                        httpSampler("consultas_multas",baseClass.URL + "/consulta_multas")
                                .method("GET")
                                .protocol("https")
                                .header("Host",baseClass.URL)
                                .header("Connection","keep-alive")
                )
                .children(
                        httpSampler("consultas_multas_consulta",baseClass.URL + "/consulta_multas?-1.-consultaDeudaForm&buscarLink=x")
                                .method("POST")
                                .body("matricula=AQM+119&padron=360565&departamento=1&g-recaptcha-response=03AKH6MRHaEHDDWFaD9ds9E0mmm5VVKhX2yVGWpH5JdgKidQFzkrJePaFfPu5PT1tOA6HI4FSVjjc14UKJ9F2viyCK2scej2koAp_GyBQr8-lzIyRZ6r_bUmUbV1E4s_mGUoN1ygD8PYx_YF3AN0suwmJ2PkELf73v7Tqyfne0-AC3thGSQutOa1rsnBrCyZKAmCtIDov0lUQl_x97W6OCtRK_IiSfW7TPoTWB5IA1cmGbKmDvuLRSgmH5nrwvOlQVPMa3cJ2j5swrwnX7vV0y_BcNCzXhF1vgq05nbUVH5DsHiyCXps_7mGVXrqSN8D0YsZ45PPxDSNq4Ewhrtmfpb1IGWhPUkaZICVMfnhkdBmNxVQy9K_-giC-PKBkAtU_DkXlZ8cpdy8_hvPAVOUzX612WmMRgQgLSxWdN-ZbzPYJaCGsvw1OubZLWILNxmvhRmkZ6kh9t5A0cP6VR2_kF7Y-3h1_ACoHiTzFqIa-4Mg6vhrsSL4qZ7Zj7fwpBzYLLFMJ9DVqjfLGfnyp8AosNjg44X8H10dEgzf8raOJvTtuvwSZDky6oOD4a_qr3dSl6OHkC6kFMpJHO_Z362nbBTEecerFSYe6RiA&buttonPanel%3AbuscarLink=1")
                                .protocol("https")
                                .header("Host",baseClass.URL)
                                .header("Connection","keep-alive"),
                        responseAssertion("vuelva a intentar.")
                ),
            resultsTreeVisualizer()
    ).run();
    assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
  }

}