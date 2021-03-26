package com.techstar.testplat.web.utils;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.LongConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import joptsimple.util.DateConverter;

public class BeanUtils {
	private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
	public static final ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
	private static BeanUtilsBean beanUtilsBean;

	public static boolean isEmpty(Object var0) {
		if (var0 == null) {
			return true;
		} else {
			if (var0 instanceof String) {
				if (((String) var0).trim().length() == 0) {
					return true;
				}
			} else if (var0 instanceof Collection) {
				if (((Collection) var0).isEmpty()) {
					return true;
				}
			} else if (var0.getClass().isArray()) {
				if (((Object[]) var0).length == 0) {
					return true;
				}
			} else if (var0 instanceof Map && ((Map) var0).isEmpty()) {
				return true;
			}

			return false;
		}
	}

	public static boolean isNotEmpty(Object var0) {
		return !isEmpty(var0);
	}

	public static boolean isNumber(Object var0) {
		if (var0 == null) {
			return false;
		} else if (var0 instanceof Number) {
			return true;
		} else if (var0 instanceof String) {
			try {
				Double.parseDouble((String) var0);
				return true;
			} catch (NumberFormatException var1) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static Object populateEntity(Map<String, ? extends Object> var0, Object var1)
			throws IllegalAccessException, InvocationTargetException {
		beanUtilsBean.populate(var1, var0);
		return var1;
	}

	public static boolean validClass(String var0) {
		try {
			Class.forName(var0);
			return true;
		} catch (ClassNotFoundException var1) {
			return false;
		}
	}

	public static boolean isInherit(Class var0, Class var1) {
		return var1.isAssignableFrom(var0);
	}

	public static Object cloneBean(Object var0) {
		try {
			return beanUtilsBean.cloneBean(var0);
		} catch (Exception var1) {
			a(var1);
			return null;
		}
	}

	public static Object cloneObject(Object var0) {
		ObjectOutputStream var1 = null;
		ObjectInputStream var2 = null;
		Object var3 = null;

		try {
			ByteArrayOutputStream var4 = new ByteArrayOutputStream();
			(var1 = new ObjectOutputStream(var4)).writeObject(var0);
			ByteArrayInputStream var16 = new ByteArrayInputStream(var4.toByteArray());
			var3 = (var2 = new ObjectInputStream(var16)).readObject();
		} catch (IOException var13) {
			logger.error("clone object failed {}.", var13.getMessage());
		} catch (ClassNotFoundException var14) {
			logger.error("class not found {}.", var14.getMessage());
		} finally {
			try {
				if (var1 != null) {
					var1.close();
				}

				if (var2 != null) {
					var2.close();
				}
			} catch (IOException var12) {
				;
			}

		}

		return var3;
	}

	public static void copyNotNullProperties(Object var0, Object var1) {
		if (var0 == null) {
			logger.error("No destination bean specified");
		} else if (var1 == null) {
			logger.error("No origin bean specified");
		} else {
			try {
				int var3;
				String var4;
				Object var5;
				if (var1 instanceof DynaBean) {
					DynaProperty[] var2 = ((DynaBean) var1).getDynaClass().getDynaProperties();

					for (var3 = 0; var3 < var2.length; ++var3) {
						var4 = var2[var3].getName();
						if (beanUtilsBean.getPropertyUtils().isReadable(var1, var4)
								&& beanUtilsBean.getPropertyUtils().isWriteable(var0, var4)) {
							var5 = ((DynaBean) var1).get(var4);
							beanUtilsBean.copyProperty(var0, var4, var5);
						}
					}
				} else {
					if (!(var1 instanceof Map)) {
						PropertyDescriptor[] var8 = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(var1);

						for (var3 = 0; var3 < var8.length; ++var3) {
							var4 = var8[var3].getName();
							if (!"class".equals(var4) && beanUtilsBean.getPropertyUtils().isReadable(var1, var4)
									&& beanUtilsBean.getPropertyUtils().isWriteable(var0, var4) && (var5 = beanUtilsBean
											.getPropertyUtils().getSimpleProperty(var1, var4)) != null) {
								beanUtilsBean.copyProperty(var0, var4, var5);
							}
						}

						return;
					}

					Iterator var7 = ((Map) var1).entrySet().iterator();

					while (var7.hasNext()) {
						Entry var9;
						var4 = (String) (var9 = (Entry) var7.next()).getKey();
						if (beanUtilsBean.getPropertyUtils().isWriteable(var0, var4)) {
							beanUtilsBean.copyProperty(var0, var4, var9.getValue());
						}
					}
				}
			} catch (Exception var6) {
				a(var6);
			}

		}
	}

	public static <T> T copyProperties(Class<T> var0, Object var1) {
		try {
			Object var3;
			copyProperties(var3 = var0.newInstance(), var1);
			return (T) var3;
		} catch (Exception var2) {
			a(var2);
			return null;
		}
	}

	public static void copyProperties(Object var0, Object var1) {
		try {
			beanUtilsBean.copyProperties(var0, var1);
		} catch (Exception var2) {
			a(var2);
		}
	}

	public static void copyProperty(Object var0, String var1, Object var2) {
		try {
			beanUtilsBean.copyProperty(var0, var1, var2);
		} catch (Exception var3) {
			a(var3);
		}
	}

	public static Map<?, ?> describe(Object var0) {
		try {
			return beanUtilsBean.describe(var0);
		} catch (Exception var1) {
			a(var1);
			return null;
		}
	}

	public static String[] getArrayProperty(Object var0, String var1) {
		try {
			return beanUtilsBean.getArrayProperty(var0, var1);
		} catch (Exception var2) {
			a(var2);
			return new String[0];
		}
	}

	public static ConvertUtilsBean getConvertUtils() {
		return beanUtilsBean.getConvertUtils();
	}

	public static String getIndexedProperty(Object var0, String var1, int var2) {
		try {
			return beanUtilsBean.getIndexedProperty(var0, var1, var2);
		} catch (Exception var3) {
			a(var3);
			return null;
		}
	}

	public static String getIndexedProperty(Object var0, String var1) {
		try {
			return beanUtilsBean.getIndexedProperty(var0, var1);
		} catch (Exception var2) {
			a(var2);
			return null;
		}
	}

	public static String getMappedProperty(Object var0, String var1, String var2) {
		try {
			return beanUtilsBean.getMappedProperty(var0, var1, var2);
		} catch (Exception var3) {
			a(var3);
			return null;
		}
	}

	public static String getMappedProperty(Object var0, String var1) {
		try {
			return beanUtilsBean.getMappedProperty(var0, var1);
		} catch (Exception var2) {
			a(var2);
			return null;
		}
	}

	public static String getNestedProperty(Object var0, String var1) {
		try {
			return beanUtilsBean.getNestedProperty(var0, var1);
		} catch (Exception var2) {
			a(var2);
			return null;
		}
	}

	public static String getProperty(Object var0, String var1) {
		try {
			return beanUtilsBean.getProperty(var0, var1);
		} catch (Exception var2) {
			a(var2);
			return null;
		}
	}

	public static PropertyUtilsBean getPropertyUtils() {
		try {
			return beanUtilsBean.getPropertyUtils();
		} catch (Exception var1) {
			a(var1);
			return null;
		}
	}

	public static String getSimpleProperty(Object var0, String var1) {
		try {
			return beanUtilsBean.getSimpleProperty(var0, var1);
		} catch (Exception var2) {
			a(var2);
			return null;
		}
	}

	public static void populate(Object var0, Map<String, ? extends Object> var1) {
		try {
			beanUtilsBean.populate(var0, var1);
		} catch (Exception var2) {
			a(var2);
		}
	}

	public static void setProperty(Object var0, String var1, Object var2) {
		try {
			beanUtilsBean.setProperty(var0, var1, var2);
		} catch (Exception var3) {
			a(var3);
		}
	}

	private static void a(Exception var0) {
		throw new BaseException(var0);
	}

	public static Object getValue(Object var0, String var1) throws IllegalAccessException, NoSuchFieldException {
		Field var2;
		(var2 = getField(var0.getClass(), var1)).setAccessible(true);
		return var2.get(var0);
	}

	public static Object convertByActType(String var0, String var1) {
		Object var2;
		if (var0.equals("int")) {
			var2 = Integer.parseInt(var1);
		} else if (var0.equals("short")) {
			var2 = Short.parseShort(var1);
		} else if (var0.equals("long")) {
			var2 = Long.parseLong(var1);
		} else if (var0.equals("float")) {
			var2 = Float.parseFloat(var1);
		} else if (var0.equals("double")) {
			var2 = Double.parseDouble(var1);
		} else if (var0.equals("boolean")) {
			var2 = Boolean.parseBoolean(var1);
		} else {
			var0.equals("java.lang.String");
			var2 = var1;
		}

		return var2;
	}

	public static Field getField(Class var0, String var1) throws NoSuchFieldException {
		if (var1 == null) {
			throw new NoSuchFieldException("Error field !");
		} else {
			return var0.getDeclaredField(var1);
		}
	}

	public static void mergeObject(Object var0, Object var1) {
		if (var0 != null && var1 != null) {
			Field[] var2 = var0.getClass().getDeclaredFields();
			Field[] var3 = var1.getClass().getDeclaredFields();

			for (int var4 = 0; var4 < var2.length; ++var4) {
				try {
					var2[var4].setAccessible(true);
					Object var5 = var2[var4].get(var0);
					var2[var4].setAccessible(false);
					if (var5 != null) {
						var3[var4].setAccessible(true);
						var3[var4].set(var1, var5);
						var3[var4].setAccessible(false);
					}
				} catch (Exception var6) {
					logger.error("mergeObject" + var6.getMessage());
				}
			}

		}
	}

	static {
		beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
		convertUtilsBean.register((Converter) new DateConverter(null), Date.class);
		convertUtilsBean.register(new LongConverter((Object) null), Long.class);
	}
}
