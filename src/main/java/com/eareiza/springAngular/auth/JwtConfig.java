package com.eareiza.springAngular.auth;

public class JwtConfig {
	public static final String LLAVE_SECRETA = "alguna.clave.secreta.123456789"; 
	public static final String RSA_PRIVADA = "-----BEGIN RSA PRIVATE KEY-----\n" + 
			"MIIEowIBAAKCAQEAsgrRmKYWY0N4VBpxlGSnB0lD9wYGUUNo0nvj6YZx+sE7F7bT\n" + 
			"eWQAOfOSHxnNm2MPPjZhCOGxqs15eE/IJlVvyqvrLIv12qgMmO1Xo6wPzw5d6T51\n" + 
			"e55BniM3FX1+KirBngYFdkRtZOpqT7/1mCeuuY/QFXA04JJ8U7QUvcM+6TRMaWfU\n" + 
			"zgh0/yDeyVs2xxv1Chg02OHNJSNL9uX89DTECiWZIS+mXPhWhrvo6nEjsaI3P7c0\n" + 
			"ycf//7J4mVVceu1vxJD2Mqn3E4j5WC1SSAx1XJTMo2AhbyZLjAlon5iX7EBOKlXQ\n" + 
			"gdJaLMU0iuW/blwVCukYeC+PMgzLhzdEum2wDQIDAQABAoIBAF6rogepSP4/Qhx2\n" + 
			"cy6U1rvpGVqguXs8fOBvQCFMfA758JILhSdnJTerbGddMRcO9Wv/PlmaqP5Jg+2V\n" + 
			"frfjdi4ufD6TrYc3FHu8uH0vYLiYyxbbP10cKYgM8Z1bvVpuocWFLiJVAFZIz37Y\n" + 
			"c58UerPw5JSBtZw0SCe5+c1tpGYbpev3hJ9ESaQHwhBT76Mjx/pD0huMY6QkABID\n" + 
			"qKg7v9RAYsJNLaQY2OwH8ihZg+iDH3oj3QI6/fBXY9LBwG30n2EC3qL9FPBiBU6H\n" + 
			"+psU5tuXIe1eRzOQQVaRi6kPHhj22zQ4+KXk0ka79rFd2pT9A1c1lTLSONeE1YPZ\n" + 
			"N8TXg4ECgYEA2lsAO1rLWGCnz7Bd8A10PmSxh6J4zlP8I/U7sO4AoEp/iJUK8WDg\n" + 
			"aJV1O2oEsCfgZKdown1UAcX3YbMrMX36nSZ5UEzXHO9if63B5Ox8+AnQ/lLvJOcw\n" + 
			"/m+Ojg+BPjyEMd4uKKgtYh1W6v2mcQDC5BvJuM3oLTugL41yhcgO578CgYEA0Lyd\n" + 
			"x9QGghyU/Q4Dn6Z4nb3/Yu647Yjth/3gZ3DlkHjjfG2zhG6HYNwc2N8TCBexTvip\n" + 
			"9PUQFWo8O/eFAI8DLiOZphb8hS3a2xn77tz5ftkCWtubJj5xCKdFtbH7FqE9ubfo\n" + 
			"Uzr8PY75PMJ1hYh1hADOLjytjlIfLD/y2ncTuzMCgYEAmMXEssKCtVHi91fhUZxI\n" + 
			"MYNIBqrxNQLjzdC9s7vBqmSVOF13K4zpVaMEQ86YextbT3ElxbXkOd1liPDPjrAi\n" + 
			"0SLjDADHHNKMKuLKXz3q7awIkng6I6qxx8MLjW9hsMasgyt9RirsndIDRySy+Ctd\n" + 
			"MDYU+/zsrgEiwUKI1vszGocCgYAPyPO1lNcZJ8gsis7DV/dvFlslxB+/9sbfrdc2\n" + 
			"e1cg4cNyOWmF7kXWkU6tJ9Fsnz4NVsijeZTJycrcTg+Ex0vaRV7tLHfUKm+OehyP\n" + 
			"J67GZOrErC7DaZ1qphujbd4rC+57COlMwf0pOpKmgZ4ZhwQ8A0OUQ4psZNFuqzdW\n" + 
			"RgL7+QKBgGUtlOO3NDreainMPuh12JfpTUj74pMh8jGlZNL7bVRHodSK9hm9tI/H\n" + 
			"7BgB2MO1KKnRM9yq+6iL6IjHWoy1ws97XByl+jmRvfqdyvfxIU6hjnqQux+nsuEL\n" + 
			"gbbPfNIZWpuo2IKFn5ftXNsw8dtlcxjhiWFsMQKQ5XQ0J1Yj5XrD\n" + 
			"-----END RSA PRIVATE KEY-----";
	public static final String RSA_PUBLICA ="-----BEGIN PUBLIC KEY-----\n" + 
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsgrRmKYWY0N4VBpxlGSn\n" + 
			"B0lD9wYGUUNo0nvj6YZx+sE7F7bTeWQAOfOSHxnNm2MPPjZhCOGxqs15eE/IJlVv\n" + 
			"yqvrLIv12qgMmO1Xo6wPzw5d6T51e55BniM3FX1+KirBngYFdkRtZOpqT7/1mCeu\n" + 
			"uY/QFXA04JJ8U7QUvcM+6TRMaWfUzgh0/yDeyVs2xxv1Chg02OHNJSNL9uX89DTE\n" + 
			"CiWZIS+mXPhWhrvo6nEjsaI3P7c0ycf//7J4mVVceu1vxJD2Mqn3E4j5WC1SSAx1\n" + 
			"XJTMo2AhbyZLjAlon5iX7EBOKlXQgdJaLMU0iuW/blwVCukYeC+PMgzLhzdEum2w\n" + 
			"DQIDAQAB\n" + 
			"-----END PUBLIC KEY-----";
}
