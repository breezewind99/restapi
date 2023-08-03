package com.cnettech.restapi.controller;

import com.cnettech.restapi.model.SampleRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/sample")
public class Sample {
    @ApiIgnore
    @PostMapping (value = "/test", consumes = {"application/json"}, produces = {"application/json"})
    public JSONObject test(@RequestBody SampleRequest jsonObj) {

        try {

//            String jsonText =  "{" +
//                    "\"RESPONSE\": {" +
//                    "\"HEADER\": {" +
//                    "\"RESULT_CODE\":\"00\"," +
//                    "\"RESULT\":\"S\"," +
//                    "\"RESULT_MESSAGE\":\"서비스가 정상적으로 처리되었습니다.\"" +
//                    "}," +
//                    "\"BODY\":{" +
//                    "  \"redFileList\": [" ;
//            if (jsonObj.cratSeq < 1000) {
//                for (int Loopi = jsonObj.cratSeq; Loopi < jsonObj.cratSeq + 100; Loopi++) {
//                    jsonText += "    {\"recdgKeyVal\": \"" + Loopi + "\",\"cratSeq\": \"" + Loopi + "\",\"resultKind\":null,\"resultMsg\":null},";
//                }
//            }
//            jsonText +=       " ]" +
//                    "}" +
//                    "}}";
            System.out.println(jsonObj.cratSeq);
            String jsonText = "";
            if (jsonObj.cratSeq > 1) {
                jsonText =  "{" +
                        "\"RESPONSE\": {" +
                        "\"HEADER\": {" +
                        "\"RESULT_CODE\":\"00\"," +
                        "\"RESULT\":\"S\"," +
                        "\"RESULT_MESSAGE\":\"서비스가 정상적으로 처리되었습니다.\"" +
                        "}," +
                        "\"BODY\":\"WqNRIeUc6YtxQ8SQWq3/wLsxcyY4UbJH2fBI1QB6Lu8a/U5qVbFkcBS7zoYHFqWtlbXKjLH8UqOZNvJIAJfDag==\"" +
                        "}}";
            } else {
                jsonText =  "{" +
                        "\"RESPONSE\": {" +
                        "\"HEADER\": {" +
                        "\"RESULT_CODE\":\"00\"," +
                        "\"RESULT\":\"S\"," +
                        "\"RESULT_MESSAGE\":\"서비스가 정상적으로 처리되었습니다.\"" +
                        "}," +
                        "\"BODY\":\"WqNRIeUc6YtxQ8SQWq3/wIXXMVyWZAoJQbk5Bv+J3cEqKAO3m3PVLezonHlOpefpUob8ExjDosofCS3PHaY/NYIHlBFcgFIwa/Cq5yZu36uJdkX9Mk4CoWZbrhYOUCh0CGBARgfbKYXan1owYVa6Ykwt8cWvVtufpm7S9XqVsn+YcDJJNQYND7DqmWkf3x5QX1dvlTidu2IZt2Pt+iJSfSv/uLLuAFIlFPRdrvQI3ewH311bAKXWKCnCvQDvVk35DIZUykZwfaF1AzLHbE0K5nsGNGnMJr1ecacKq/OGifql9KX5vUH+++T2+bvUfjL8XbAQ9PaFVe58imTfqdhcscO81OcBYWE28Hzdo3GpDjBbEdhUsaQfA02YiwaCWDGyOkSN69zBWsC/4Ql9GJM4PRQni6yvFj225pTq5VwgouG/Gh+ZwZAOOQn6xHpWekfLl6uIPco8lUUZ4P3Tjl/oJe/2i8VmZcHPkD4lAqWees/oFQ9NlplaSMs1z7xEpE4G8dbFzHs5nWKFt4XHBA9sh5VxG5rSh0ir894QDqUxV6vr5REK4TxEL5rJbIOM9gmKMFKWCCfmAu8AZ8CkdnZhgiszvaji2fNi152BgLLuPw9WIys62H8uzl14GviCTlXKArReyPVxZ04ZLhhFyuohpxhgj4SnoPwitwRz1KUzD78VmGwYqtPoKu4zA2r+xmX+YcDVDJZ6uxDFnG1QtW4Mt3eUcys//rlNiLEm2+4JR00sbvDY4jvvsrf1VrtXgoKrzEgOkYJiPEwCbCX4EAjZbc3ACiOBbCxACsQ12TOhZRJzlI+nHspJDg3EVYSKStJpbmbd5VTX1U7NmAkChPorCDHQjtg4rZm+UEhCiwdQi6HqZugPr/hxZbJvFu5jXawLnrdGevkeuQjUyfO84d8LiOc+CL4yATkyW6A3oCcdeeZC5lk8jskj4VordWkYjNZ1rOfdmWcoYsia7ot+Si9WisHQ3Pman/hJiZ8RDeRdiw7D49ZIkb4He6Jxjea4Z3bCoovfpa1qvD2D1DYV7hfraZ5sGvzmiCVO2rGm5zSwN2Bx9+iE4GAaRIGFajVofgrzxQR1myMtZT30lrKCD7aJ1juuohw+/xs0jgiGnEv+HQpEVbbF4iRw1d7xjo20KsAQQzog6kdewhEo49F0YuhPxdHGFc9s5Kra7RYfuxaLiwWQC4MElOBI3Y+L4pFTerRExJgxDtmlcu49W9W1ftce2wMLqyi/aAQLwseMNLoWWkzrPjAxrMaKLNvMYLzvc/n8ANxzmattQqksRCaeGhWZiLLu1TSGrfaFT1JZBcJIzOaaHoQcEHtWSXL7G3ek6rXZEi8jpQ4IgvPh7GVNuYsqqh9BcS9IbNkIGZqp6umE8XjnHdTd7uCFZbA3Z33xhtUmMgI0nFvAekqr/Ry/yKtaj8RyjGWFDt7IUf3OvxznhzJKe5scT9wSreiI9YQQewIFWRtb+JlFac98HUMMgVBq1oeGj1A392wwr10Ni2y+b8xUHiAaulwmJtXD+g84T65uoXd7d8lPVqJ5r2IsnhGsrgmEa2Cwt9TFAca5wgAyPKh6daXBJQXCujX9w93yyYaNE+TNhGG/5jOynVo6sJygUl3Zk3fIubIqv3QqETMkjT2RpsjaxIPb2bW8vgNz6vCSUlCspzq2FHs7CGtOLbK1VRcRA7uyXP8NIJczamI1/khibmlKe6iNSXk8Ars1GZrRQlOLh0VmKSrKstjfU8Hb5MLM6h53GVt2RFaisAa1GnRjNuz600cb/b8pxvBODNnf/5lJvYQylOdjONouaYOEZFStNxznORfAfWJuC3EJvSzYeSIpl0EExYGP9os9lmspaRfuCmzpO4dPO0+T+jymNWjElD6xmvNKmzke0zazvAn2dxL1EG0Xa6J+ynzZBSq8ihfrO1WxWUNSlmYKsaVKJ7E1s1xtlKCWEZBaa5KF6aDvBfHNe/q+yj4YrVjgiuavcu975RlFIH9jXOCZ7ZhQRuN+C8Lc2F5V7ZP6mjOq3Yd2ZQVMJUOI41T3ea6DGTYJROQUdm8K6T6SWVmMExgEX8GE6uKeWcfOBe8WkaQWviGu0REKqW9Qwf3XwqD5F/lTNdF4aZ8jLpqZelBccxRFb48fC5xztB1Kqi8Vl7m4A138ylP/fbs1yFtTOdKc/ekSTU0/QwckiPghXRc0AUmB5/dfWSbQK0YRkqTuTGDSk3oKmmtraR/c3ocYU0rm1g6wg1aTNA+7ICabSI64d6vB9dw8rhoNv/LYNUle7phDGTYvC3ZLml+CeAE6zQYFv0p6POZXilb9CGpaDU/rg8r5NEAk3PKkl869uy2ysqjlyUomRM/6Dmlbf2DK5wM7mf0akQHxcNH6e3DaCDsEMV8sFjvEwonaa3q2o2sg7QEcTPMpRdkG4gyBmQQxCDUeOGt9IQByLaLHtgP123jrJGuALNEMtiYJaWfIA8wAPCgmcl1CMHQHE6WYyJbWuHm6awb5YttXW6Dy/P9R9g99yUy4U3tDCclrMG/m/DZx+txhUeRGa49KzR1MnyUfRmLgD3QEaetQrT27Td/qUhAG4TjUjB+sDQrs4ncCn/8nbV5NdAQRW4+galMjaVeaB8g0ZWCJ6aU3vNb+7kQzj1VAwfpuZwzCeHbZvG1HzMR9UdtI4ZE0yt+By+yMovmPBgTsaihRTErHOJusbU5ITcrkm3gxZ+coLGaYP9prOcO58kCPsyuRKyZCBm+5eE4SRXmmXKzJCRY1JRnqgNwhW2+hTsPSp7i55vOdPWHno9/7QZnxYks/DnMqEhw3mRPPVel7ouF9iWwFHeCy3jsaBSu6Enbuq2o6B8kSkimfOnxiXFEIv1TaagSTQYENeFEVBxpJhEGKgFA4rCMrcyuRcBsEWhoSm7GSHetBdzqgMypmVLTYROBUcc3hYhClTXeH14EyEn1NN6JSItVStZLLv9VnfiWE86hsNM8WDDP2KMMsQ7z7zYObzTolxBLwgCYyPf4nDPbd04gIVzWwxJsqYo4A3yqkLJKowwvnzCK1ziH14mwNfpdPOIAVHTJRvlgLK9AEEs/GA1nxkpXGGZ+/j083cho5eQ6EigicG9f8khOpxm/66lPpoQ+6ZKrSHScL8wnQzVRGK1XW13jExtndV0J3R5UJXgB9nGUnNJnmQ+BH0USEbt8X+bAndgqR8IW5S8xokBPcYZQXxzbj8cNnke7x2yfc2VC+WoSoNdtqm/+lWlAbZGykC1et3oHF/lu6YpOmPhFWe/QN6UGUk7nAchAY4QyOglUAMVC2aVhdXbpziuzHfX8lG7zRav5vTHXDWuAbncDDS1Q8ydw9bWYgqhkLjFOMKnUtD2PdvZo96Ev0KVjgAcYG7HvWX7Yh4h+p184YNPodY2JZIlTHIJRqjWsZqUSFclksIDBw28UDEc9ftApexSlka4kgz36MAEiDdLowtSnaMuwpn4haJCIC/7vCuTqh90N7ZCcjvIJuRN00jJx/JGp+cNigJ1++21MbjFWYUu0W47YP7vmrI99OujTPDqBcC/2rtx7h31hrAuK5flwTSeiwWm+kWrsaj+0elvrhcEU5cS35w+nvqk5CfNFIYI04lYClKm+aPSzTjtpBx+bTmnWTeNOi3JUgZTHhokePO0h+niU/W7IZh7da1OpXzTTIsNi1fT9rxjHVPj8TV094GD5XtNBGF9JjWph+1lOybxjKVvjmuKSTL8H8vrbr6vUe0Zp4ZhflmmZZ2nZ6S6gNFOgbP5LpByfoe6+TiH+mYIwK68I4ME0WiK2I8Xd6i8fxZCWAFBjiDBWcY1kVRxDzViMvwDCMZBGtzylNNJ4Z4fxptAdPRxi8sYxN4UhVyyZ06XBe3Wjh6PwczvqFhQ6i68kdRGSTozkMKQUEbrGsDd82Se5S8+c4wY0mh1J4uZ41NyRuSaRKWUL7svqZsdtXh6DqHLoYu6qiwfU+R51Rbs44d7maDDNGqSPET6wuTgg/vKlmvwkAMNOEsW3oJre1hdJEo7lygS3iJEiBPAmVtSi3nMxS2lqGmF9833+YZvmt0K9bZqt4DIzfpGksIKIimE+6bLXZwJwW8NVwpbSK08m00CKDYs9dwY3DUc+oJ3LdJNy8L8c7qvz8NE93fS4Y7fsiMQNXMBgpG1bfY+lADHWUQ3gqhlQd7P8Qo+wEu+smXV8qwbIXa8T0VdRSP2VbFiZiEbDbFrnbW5FyYWvfqLCYz1eQ74+LBzdvQ1c9qr1ittkvvl15GQR7OhZsgokCAWgnmryYQqKwuy7uNqRu4mMtMsdGTE7AegfLUen1vxzO4oTudT5BPkZLOLkixkHibWMV9v9z9L3JlK3IVWnGt1hHQZ5P3NeusG6FgiM7EvzTm3PMzjicMK4JI/j61z707AYxEaTqaWGwOH5bWH9ZKYK2nSdzTE1cpRb4klVB6RyqZzgdHj7ZFHXNmimA2t+wj5Gmghqp4cNub9gHtAo9D8SDKthL3xYAVt+gn3wej5uk+grjxJpAVT5jOVftuT6Docm0Cj0pC5lvxsmNH1aav99nNwmlquXqgz71BG0cB+P0eq5lZo4hW30xZ3raFdyKmPo/aJDNZ4BlB0d9p+FZu2OfdRoH1PN3DvaT05URWpMBcHWLRhqov1O85fSL5HhqjSVR+u4rqL3H8PRs0XQbotmVxzt0X0Uune+6DGlNz5lHQzTPnwx4GHhhc/nnLW2i9+Hzw0c8Po/+eqbTGYZUdoozka3Xz8w5GyAwMIfcH0UE/a9wSJBrQSoItf8YfQIHGvVOii2sNbC29gd7bP3a+viL0pmOsGEtpH4QvUx+roRIiD1d10dFg23g0vY/y4QW154NUH5a3PBGAy40RWzbRstboD+8kbWC7HMqWuP+zQXjVPxmu3w6/M+qMYBspP1v0iWGQKyZbMJtbrWsJn7Il7awKH+HdIlf5OvriIDtpK4qA7FOXf1U1E2mNcsm4a6bthFtrBYNtGbrsHVJNcsVgoYIYYpFcl9OxaGDl1boKK2pKE+1Y5ba8NmZctfUEacXHtgllNHKidBzf4WmK9lVXCJa3i+un3JbGJnlkO76usLCXJzqyiEyE+QVZ+GmgGZ3bN/mwrOfZeSXWo+ztaG6pNfbF9MYlZaiOuCG8iJBjEevrah58/6dhHRc8UaAgnXsR/JYlJ63Myliw4EOyEhO3RCYP7qmOacnWgmhpNKyfBf88DSyr4pjc6M85HAaXPA5P4QwhQyQJ8nt1y7y2F0b7iHoenx4hXpW2hdt4vHWpkUteYrtBnFkGqvLGLAjlNgmFblSOySjYRYR8jbSqsfWyWVApFVzDtStxUkimwGxLX6HQVGjdntPdAp1kMekFRSqxKglu+xZkRJW7An7KWf2L30Ww6fuUGVBnKAqnj5Y05La3U6TA4RAqKSmE98IHwHoK80scQg8JtDE9Z/KVNcm5/ME6YDSHtWn7vX9k3ByzpKTLl9c9A/JIpL870R3k2e1nqPjdGeJjY1fdS2JEZZ2VqE22gpKGmbwWyUHTIXjPAvA8KJFM6FgGVSqxg8iq3CJ5uXRyv8LIDzJgou1KOrcHM15/I6fIlFQdmVSXvAliIFhq38jFp5NONyQNA6QvvTBwaFPJ7YTpGCH8i45XuxdRAks6ww7Mbjmwt+UBvZvWx1huhIV35F8fOPfoJ2l9rsA6j0mc2kuCRUJCoKm3iAKr9HPh+/N84h6nBlV1nTE5eiiZTW6tyU8OSu05+LNTp3aghPLFcQ4PPqzwYpM8/k/aYNQTHv0S2Hz0QTz6K5ujOVjxL6OPY+mzaYJPfX487u95U/ik8V+ZNKsY37a6yfnodMkNYo8+50I+iAQUHHo8FRKPzFl/cAtE+JO3dKzcWHSfLmgoGYCwIgJI4KsVn/bei8kVgxTBLwTd5LfLv2e6KJxbqU8duUDF0WoiNoHsDxUNeT6eYbhu6BVFEUrYMQ7xfahZiBkjYcgtA/z+PaV5zo1s2mXnLcSCW7S3a3bHE0mI35Fj/iwtxrvj0GxcAak8/MVQZcfv9zfpCFiC+frb24G80uBqOBGzuhaA4bIV5O8dep0TAB+mWKZCKg28dxz+B2UBu7p9HgrmlYuTXIQOVNuAOpj6U/HaUrQ6osDKOeiW5LjY+e0mA6HqSkORFUTe7GzyB8MDsfb7SzPxrap+oz5OB8GtJ2Ldg9UlWi06LUB5zCh16NoyocwkqW00ZMSMMW5xaVTPCU7Ao7/YXbAOsjq94iQlS6rCaRSmd9VwlS62aa+c+xev6H4IXGhe0DafKBaPd8kcGPKrgY7+vWCKwV7qbghzrrac76gD0om7zzVB31Xvr2uz74w/8BiBBVsnOwr0dy/A8sHV+7/QqcVjw+EENY44vY/BGaSnu4su1vYnogy9v3uNukmB8REVnFAYYrZG068t+odYxzTqVQZ3vhHIfUZ7XyTh0JCBFFUJ85tXqQhS6eoMw3qS1bSEoCBRAYLKxvP3f00ckccTVapYXnuQgmYaTH9uivmxfJ9Mbw3IdkfhYJm7fZIRgO0xN69f8XBbueC5MiA46HiujQ8QB8oBiB0OwhXQS97SUFP955OJOhV+gr8CQKA/Z/Wg+FXmlIBguOhmCcpcHostYDssMk9KoQrgbZvsc4EPt4gVYC8Ow0nVM8xXYJqFB01M3ptQHn/wE1G4pbZS3+6UXW/TByE/TfyoMU1uYM15L0ertNVxcieYBf97o2+ceX5S1X/k8g8yFHOg/E0jLSlJSCHeyZrSSLHkX3PuGxdTY2Kwz57oLAs7R4zo/1lcA2oTyAbuMAurT7rJNYKYvwfi+S3fHG/fKoXg0HFEabCxYSEdR4wZNaFP7bEysRvRTGX3sdtHNucYZ2AuPUaw8vgbH7BwM+rUBMI7PsmYTyXZsgbYqHARHYK9OvjdJGjRSee02kLo3q+iilH/A0Zcc7AWaMziUNPENCOVZZIbfWubsJoidGa9lGGoGjIyEFNzGiCioInMLLTdFp1FUfFOI3zI1mBPXzlARbyk23Nxp8azJrLub6BKr1Vdr+ydjH+pNkykKj4x74fiWt/BSYK9c/p7l4DXeg42ymtwVpYXgXvXY99JYqM3SPS6+yvF8GwOl2fGAR5E8E3kw7Cyg3gVJE9RfBgf/BXXHZySGX3cCwZRZjVTj7kKj87xplO54ZVy7+D5VMKg7x6QThT4+VLN76MMTgNjQN61lzpMQv3725UbuooNxzs25B2VSuT6ENmOwp5XqaatY6miLh1T49e8r+1G80DafybDIKSOwXMUMCEhA6hXX2qWCmLbAF4ChzhCbVuNf8/FECF5zMxW2lZggbmnRdFfUIaqBw0dwLUgmE40sU9w6EUCXrfoIOAwGCoJwR7mBdhYpN0YdPIuwnUXWn6DvmLjChA4a/SuYOYCLmjIvAeDzG4IqhHwoKJCoOs8H2bUNM5fxmx8s2O0wFMOzxJ9iUeyw6iRfOx3K8O0xRJGNFyus47kNbMTGjX94rKycjw4D+XnU059K3BKvrjbKGWclV8Rio9hlkDitM9iDDGLjKZbvAIkMevSnlqjq/Cm8HFbqMQoVl0a1OAnZ/gwTWUl9K4jUxLrgQBERlJZiH1S0BJ/h2ljdshm2BJux4r0YECncdvN/a7eLLcCjr+n1eTydy7qgzglS9IcG97MV608TVDFtqot1+octoFZECApyrAankXWwREH665LIad2+HpSPTy97dB5bMN0SuZRwKPv5STDX2u3G1ZP3PnVXp5G79nqGayLTAJGsJQlQNppujHhz01FL48poD2wM1fOtQXRj5OoKCFebECOTGQ023cAx1tNHySUzBTQHld9u1YmsVVT7aFaWMwqCQBqZWRA9Td+KQrA9uEbpSoqtzaZiOogQl9v/gbXtzgGg8x6Je37KCqkeU3JCQ1OL27J9IQhWZq9CQC41p6GfYEbREqLigrDsl/Nce4eKOmcr/LInlNx27N+ibzMVtEkUU4LgHjqkRl1ntU+php2wRVCVQAY/PzIWixOC1wVIf4CuzN5nO23V+Y8CbqH5K6Edf14uswG3ns3wImrdeMAj5xTrp2XvfiZWNFrAjlQTTpLselGCMqIPhN25JwlRePkK7GSwOZtMAFnjbgIdi88NPy+8hg2eVF9wMBWfZ3GBsY4QFwBSujm8jp+CABaDq/ulVIk8jxh7N6zZ/BmmOqPcd3yQq8Y9TE3XagmJJA/ydR4Nc9OwJdS2qEjif0CTokTeSVnbvGGZZC8mjPgi5pLWBhkkOkkWjYUMxWDqw7AfjfR0b91FMVrJB9L+CHR9uEVSPamq/Rc6hC0PC8OHUeLpGQ7Fy9JHmOA3aX745oYqxik1RQUVu4ZjlNv4oSghxF9h7QXcCbaPQ8QZERSq1FS5HEaR4Z7OZhGKs/vXxxPk0BtcTbf6C0z0dw4TGfT2sqxiJoMwEjSKU1ox/7OUvd4Izykjpsk28W8qz9iyXxnY9etAy8bvE1S34y+miWdyA6VBiIiT2uuc9WJGvHn7mW2UC0UsFKdwCzgb10ZMhqrRT0neNRmUMtghfoKkMf1BjaiuZOUDI+9wINEEq8DSK4lSNYmYSLBSzo95lazNlb6/+bzNpRrusL1swDo7UU9FK3E5Q44BfgNP8jHrOQ790lxmcIvLHuq+gxP6ynUUmMiH1AIj6+5T5NVenJq1iOZLpGZzO8fyBHDAIuKiw0Zfn/5VwoSNhD9rMoq7amXL5sW8xZP8U5hTNRx7Jqo77xEiEq/r5HvulKmvcdDuI5lI9BnYXQ4ZT1+waO1SCFgrm0a96xeTdDLyHEWgHAuCdOtx/Cog2c2MD0bELCmUJezVDHpxq/XuVtY3qeWpAxIFO7wHd9pldQ0OUnAvHyS6eAeNGBHK6sZlLuw7BxEea/kUO3Mi5sVEvcCj9GlDWCZA5+pQfSNi9Lz8BEnaPPCHXSgR2XyuCkr0eaO6YqyqC0En7wHrs9NVxcBcMW0wHCfE9wELdJTzVtowXPMZiYVrOlqcfjh5GPRmdZyU6Mau2wgAluj7aDBYuMRcOEcduyW76aH8GGK5aaxJu3cifhh6d4KE8k2k46j2aaZC1PxDQBS3W7FN+yDjY6zGUKSZpyiFXqk6NQLj4CNPGAiDYaPPj6K0V5ogRe2MN9vOIFXBAoTwvA0G8abWKzz/mOfOXyFRwz0wvhg/9t9tFxwYCmb4bOHdjttyNyIg5cR3imvyDAQwTNxUTOQIwY3TuAMuaIqAh6l5wirSGeJJEW9IWwMtpxAoVvd0RmHBpBmmUq/tEDVE5CngaYylhyZlvZGOJ7cvPd99u39DNYipfxuWRH4IXY0gSULdbpVtmnRCOTH58R79c3FSjCemUH1ewSCCb3KGMRJLOFKpJO57nlAc4snxHhjfn9J4btDk9FJbKaNhjU4/ouF5VqpYABgO0xHRdiMPX7Q5sQDrfXLACxB8MOSgRZRonzzq+lprlVOZYraOabhkH+fW8qf/5f+yXGBbqK+N7zbVS8zu5JKWhllQBawk3BXACYohPYOdDbpgIr0lVFRSr/vxftmwHAUu0Xt/T0KPMadh1S+1RMQFmESlNsbv8JPIUSFb4mqzbNC5f1Hklu3gKDQ05UnrnFAJEM8giiIaoptCIOfanDZU9infS5FQr6MZSxORnYwpqfZ6rD8lKsyhbnbXV/pyAs04YgxxJw7RQq5gdGTvQ5+TEyhQBmu4tFaQPY9RCKF+++zx/IyYc4NZuNijtG3O4lVTZ209i5Zn5OLZAM67v6aKT93tqxhSMvj/7KQyMG7ikOp4paNeEmgB1Ze01A6Yoe/xuQ6RVOQH1xVnnn4Lon7KVgDrPrXNh4yXggneYGoX6uYWaaw7skeXadbCqs83uJuqS7LSgqHN5qqFboWwa55iDH2RShAybuyC5Uz+M3pflcuavbwwDn3pXf4yDJHC+YkGMtWACqfXVeMBcxrkHgeslorHKs70sVvbXRtUgcGQ5Bbt8QbR6t78lqH7xH76F/kMH3bhh3ydX6MNUHFVt895gtiCUuFyq3A45g1c8/F9b+uS2x3FFsuorHZGAm9NSRvlsz3PUt7itEzO7WVPQq7HZxTqfvO24ZPBDR9+mQcAmmZEweiinM9XaJOV6CZ+sFW7o+IZMuN7J7b2s4CieGqPGGbUvTiPb1og8GGS5xoZg+xxjzGvd+oXBzcDbu0hgnVCKMFOg5GS53VDNatwU4BxdRuXUR22XX/tDbj9Zu9lN2oWqcKU1iEbtpjfySLyc8iRJc861Tiac0AtEWD4rG6rsSJIGv8lwKihw0FsePKqdIRE/YP7e88Wlxilv7yPf17cGLtd87fcH0ywsX8LtWg+Mn3ns3bFeTwGh8Tn5UuX97dbqEL6MYKME/WmWox59OaN2t1ltXr+EPA7/0OVCgqkDQvO3mKHfP0Fg+I/qhqRhDYrjFGuWKc48qFFjRsME6TReq+Kr4EWVhcaSq39UeA0QZrqCWPQI7+HwEEHZBinUT8x3ub4zHYRdgPrlikNYIr4zEKSI4z819rsv8SDQi7ZTmfqx1aDgkZCKXfP0KlkAc/wuu2jodxswLWmpBOKU9eVWxRP9XJ7S6aXTsz5fiACN7wiy7C1oYbn9B7wF35dAnEEUTHg0jAwLbA+/HUYwDpgYDKx3nemrEGYGepqkfJsG2ZWRTHR0GgzKUeaWxtF6XEeB2o3m9Wucma9v3Jo0XCZ6gnRggBItUwx/FcMCO0SJXGwVglunqTBo0l3ET01hPziXeq4B4YcGv01LZUFE2B+2mW4lvPH1YXmxBSeWBDde3IIBb0aPMW4vXSj4P2EaR8N/pJH14WLMDZIzclXHbyc3wF2Hy1l94BCY4lheD5YnMsymuFg2MybOawM7NRIa71MCqQfYQFb6j5kfp0HYrjOoAN2k9lhr7CIID7wnPn2ls7GeLzr3x7pX+bSKdY+YhhOT2sF12YFBzlTE=\"" +
                        "}}";
            }



            JSONParser parser = new JSONParser();

            JSONObject ReturnValue = null;

            try {
                ReturnValue = (JSONObject)parser.parse(jsonText);
            } catch (ParseException e) {
                System.out.println("변환에 실패");
                e.printStackTrace();
            }

            return ReturnValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
